package com.wangtotang.ttchatdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import cn.bmob.im.BmobUserManager;


/**
 * Created by Wangto Tang on 2015/3/31.
 */
public class CheckActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自动登陆状态下检测是否在其他设备登陆
        checkLogin();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //锁屏状态下的检测
        checkLogin();
    }

    public void checkLogin() {
        BmobUserManager userManager = BmobUserManager.getInstance(this);
        if (userManager.getCurrentUser() == null) {
            showToast("您的账号已在其他设备上登录!");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /** 隐藏软键盘
     * hideSoftInputView
     * @Title: hideSoftInputView
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
