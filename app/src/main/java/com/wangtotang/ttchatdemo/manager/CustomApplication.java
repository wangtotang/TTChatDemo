package com.wangtotang.ttchatdemo.manager;

import android.app.Application;

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

    /**
     * 退出登录,清空缓存数据
     */
    public void logout() {
    }
}
