package com.wangtotang.ttchatdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Wangto Tang on 2015/3/28.
 */
public class CustomApplication extends Application {
    public static CustomApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
    }



    public static CustomApplication getInstance() {
        return mInstance;
    }

}
