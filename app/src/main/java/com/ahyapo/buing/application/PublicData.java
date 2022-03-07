package com.ahyapo.buing.application;

import android.app.Application;

import java.util.Map;

public class PublicData extends Application {
    private String URL;
    public String getURL() {
        return URL;
    }
    private static PublicData mApplication;
    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public void onCreate() {
        //阿里云：121.41.210.138
        //URL = "http://10.105.216.32:8000";
        //URL = "http://121.41.210.138:8000";
        URL = "http://192.168.43.206:8000";
        mApplication = this;
        super.onCreate();
    }
    public static PublicData getContext() {
        return mApplication;
    }
}
