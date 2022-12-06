package com.tourism.attraction.utils;

import android.content.Context;
import android.widget.ImageView;

import com.tourism.attraction.R;

public class ImgUtils {

    public static void setImgFromDrawableString(Context ctx,ImageView iv, String drawableName){
        int resID = ctx.getResources().getIdentifier(drawableName, "drawable",  ctx.getPackageName());
        iv.setImageResource(resID);
    }
}
