package com.tourism.attraction;

import android.app.Application;

public class App extends Application {

    public static String package_name;

    @Override
    public void onCreate() {
        super.onCreate();
        package_name = getPackageName();
    }
}
