package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.config.Config;

import cn.bmob.im.BmobChat;


/**
 * Created by Wangto Tang on 2015/3/26.
 */
public class SplashActivity extends BaseActivity {
    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    startAnimActivity(MainActivity.class);
                    finish();
                    break;
                case GO_LOGIN:
                    startAnimActivity(LoginActivity.class);
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        BmobChat.DEBUG_MODE = true;
        BmobChat.getInstance(this).init(Config.applicationId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userManager.getCurrentUser() != null) {
            // 每次自动登陆的时候就需要更新下当前位置和好友的资料，因为好友的头像，昵称啥的是经常变动的
            updateUserInfos();
            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        }
    }

}
