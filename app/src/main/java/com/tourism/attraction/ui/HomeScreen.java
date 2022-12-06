package com.tourism.attraction.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.attraction.R;
import com.tourism.attraction.database.DBHelper;
import com.tourism.attraction.database.model.Attraction;
import com.tourism.attraction.preferences.PKey;
import com.tourism.attraction.preferences.Prefs;
import com.tourism.attraction.utils.ImgUtils;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvList;
    private Button bnClear;
    private Context mContext;
    private ListView mList;
    private MyFavAdapter mAdapter;
    private String user_name;
    private LinearLayout divFavorite;
    ArrayList<Attraction> attractions =  new ArrayList<Attraction>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initObject();
        setonClickEvents();
        loadFavoriteAttractions();
    }


    @Override
    protected void onResume() {
        loadFavoriteAttractions();
        super.onResume();
    }

    public void loadFavoriteAttractions(){
        user_name = Prefs.getStringPref(mContext,PKey.CUR_USER_NAME,"");
        attractions = DBHelper.getInstance(mContext).getFavoriteAttractions(user_name);
        mAdapter = new MyFavAdapter(this, attractions);
        mList.setAdapter(mAdapter);
        if (attractions.size() > 0) {
            divFavorite.setVisibility(View.VISIBLE);
        } else divFavorite.setVisibility(View.INVISIBLE);
    }

    public void initObject() {
        mContext = this;
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvList = (TextView) findViewById(R.id.tvList);
        bnClear = (Button) findViewById(R.id.bnClear);
        mList = (ListView) findViewById(R.id.listView);
        divFavorite = (LinearLayout) findViewById(R.id.divFavorite);
    }

    public void setonClickEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setTitle("Info");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes I'm sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    Prefs.putBooleanPref(mContext, PKey.REMEMBER_ME,false);
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
        tvList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gotoAttractionList();
            }
        });
        bnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearFavorites();
            }
        });
    }

    public void gotoAttractionList() {
        Intent intent = new Intent(HomeScreen.this, AttractionList.class);
        startActivity(intent);
    }

    public void gotoLoginPage() {
        Intent intent = new Intent(HomeScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void clearFavorites() {
        DBHelper.getInstance(mContext).clearFavorites(user_name);
        attractions.clear();
        mAdapter.notifyDataSetChanged();
        divFavorite.setVisibility(View.INVISIBLE);
    }


    class MyFavAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Attraction> attractions;

        public MyFavAdapter(Context context, ArrayList<Attraction> attractions) {
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
            final ImageView ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);

            ImgUtils.setImgFromDrawableString(mContext,ivAvatar,attractions.get(position).getPhotoName());
            tvTitle.setText(attractions.get(position).getName());
            tvContent.setText(attractions.get(position).getDescription().substring(0,40));
            ivFavorite.setVisibility(View.GONE);

            final String title = attractions.get(position).getName();
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeScreen.this, AttractionDetail.class);
                    intent.putExtra(PKey.ATTRACTION_NAME,title);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
