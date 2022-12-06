package com.tourism.attraction.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tourism.attraction.R;
import com.tourism.attraction.database.DBHelper;
import com.tourism.attraction.database.model.Attraction;
import com.tourism.attraction.preferences.PKey;
import com.tourism.attraction.preferences.Prefs;
import com.tourism.attraction.utils.ImgUtils;

public class AttractionDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack,ivAvatar;
    private TextView tvNumber, tvAddress, tvNote,tvTitle;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private Context mContext;
    private Attraction mData;
    private String title= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        initObject();
        setonClickEvents();
        loadAttraction();
        showDataInView();
    }

    public void loadAttraction() {
        mData = new Attraction();
        Intent intent = getIntent();
        title = intent.getStringExtra(PKey.ATTRACTION_NAME);
        String user_name = Prefs.getStringPref(mContext,PKey.CUR_USER_NAME,"");
        mData = DBHelper.getInstance(mContext).getAttraction(user_name,title);
    }

    public void showDataInView(){
        tvTitle.setText(mData.getName());
        tvNumber.setText(mData.getPhoneNumber());
        tvAddress.setText(mData.getAddress());
        tvNote.setText(mData.getDescription());
        ImgUtils.setImgFromDrawableString(mContext,ivAvatar,mData.getPhotoName());
        float ratingVal = Float.parseFloat(mData.getRating());
        ratingBar.setRating(ratingVal);
    }

    public void initObject() {
        mContext = this;
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
    }

    public void setonClickEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNumber:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + tvNumber.getText().toString().trim()));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Log.e("AttractionDeatil","not Found Dial Activity");
                }

            break;
            case R.id.tvAddress:
                String url = tvAddress.getText().toString().trim();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            break;
            case R.id.btnSubmit:
                String totalStars = "Total Stars:: " + ratingBar.getNumStars();
                String rating = "Rating :: " + ratingBar.getRating();
                Toast.makeText(getApplicationContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
                updateRating(ratingBar.getRating());
            break;
        }
    }

    public void updateRating(float rating){
        DBHelper.getInstance(mContext).updateRating(title,String.valueOf(rating));
    }
}
