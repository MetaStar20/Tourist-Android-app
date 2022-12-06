package com.tourism.attraction.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.attraction.R;
import com.tourism.attraction.database.AppDatabase;
import com.tourism.attraction.database.DBHelper;
import com.tourism.attraction.database.dao.AttractionDao;
import com.tourism.attraction.database.model.Attraction;
import com.tourism.attraction.preferences.PKey;
import com.tourism.attraction.preferences.Prefs;
import com.tourism.attraction.utils.ImgUtils;

import java.util.ArrayList;
import java.util.List;

public class AttractionList  extends AppCompatActivity {

    private ImageView ivBack,ivOut;
    private Context mContext;
    private ListView mList;
    private String user_name;
    List<Attraction> attractions =  new ArrayList<Attraction>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_attraction);
        initObject();
        setonClickEvents();
        loadAttractions();
    }

    public void initObject() {
        mContext = this;
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivOut = (ImageView) findViewById(R.id.ivOut);
        mList = (ListView) findViewById(R.id.listView);
    }

    public void loadAttractions(){
        user_name = Prefs.getStringPref(mContext,PKey.CUR_USER_NAME,"");
        attractions = DBHelper.getInstance(mContext).getAttractions(user_name);
         /* using AttractionDao
            AppDatabase db = AppDatabase.getAppDatabase(mContext);
            AttractionDao attractionDao = db.attractionDao();
            attractions = attractionDao.getAllAttractions(); */
        mList.setAdapter(new MyAdapter(this, attractions));
    }

    public void setonClickEvents() {
        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AttractionList.this);
                builder.setTitle("Info");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes I'm sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Prefs.putBooleanPref(mContext, PKey.REMEMBER_ME, false);
                    gotoLoginPage();
                    }
                });
                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void gotoLoginPage(){
        finish();
        Intent intent = new Intent(AttractionList.this, Login.class);
        startActivity(intent);
    }


    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<Attraction> attractions;

        public MyAdapter(Context context, List<Attraction> attractions) {
            this.context = context;
            this.attractions = attractions;
        }

        @Override
        public int getCount() {
            return attractions.size();
        }

        @Override
        public Object getItem(int position) {
            return attractions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = (View) inflater.inflate(
                        R.layout.item_attraction, null);
            }
            ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
            TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            TextView tvContent=(TextView)convertView.findViewById(R.id.tvContent);

            ImgUtils.setImgFromDrawableString(mContext,ivAvatar,attractions.get(position).getPhotoName());
            tvTitle.setText(attractions.get(position).getName());
            tvContent.setText(attractions.get(position).getDescription().substring(0,40));
            final ImageView ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);
            if(attractions.get(position).getFavorite().contains("1"))
                ivFavorite.setImageResource(R.drawable.heart_fill);
            else ivFavorite.setImageResource(R.drawable.heart_line);

            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isFavorite = !(attractions.get(position).getFavorite().contains("1"));
                    attractions.get(position).setFavorite(isFavorite ? "1" : "0");
                    if (isFavorite){
                        ivFavorite.setImageResource(R.drawable.heart_fill);
                    } else {
                         ivFavorite.setImageResource(R.drawable.heart_line);
                    }
                    updateFavorite(attractions.get(position).getName(),isFavorite);
                }
            });

            final String title = attractions.get(position).getName();
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AttractionList.this, AttractionDetail.class);
                    intent.putExtra(PKey.ATTRACTION_NAME,title);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    public void updateFavorite(String attraction_name,boolean isFavorite) {
        DBHelper.getInstance(mContext).updateFavorite(user_name,attraction_name,isFavorite);
    }

}