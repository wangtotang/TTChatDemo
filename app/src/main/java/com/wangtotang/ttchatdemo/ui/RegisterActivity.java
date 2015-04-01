package com.wangtotang.ttchatdemo.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bmob.utils.BmobLog;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.util.CommonUtil;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wangto Tang on 2015/3/27.
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_pwd_again;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTopBarForLeft("注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                register();
            }
        });
    }


    private void register(){
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_pwd_again.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(R.string.toast_error_password_null);
            return;
        }
        if (!pwd_again.equals(password)) {
            showToast(R.string.toast_error_comfirm_password);
            return;
        }

        boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
        if(!isNetConnected){
            showToast(R.string.network_tips);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        //由于每个应用的注册所需的资料都不一样，故IM sdk未提供注册方法，用户可按照bmod SDK的注册方式进行注册。
        //注册的时候需要注意两点：1、User表中绑定设备id和type，2、设备表中绑定username字段
        final User bu = new User();
        bu.setUsername(name);
        bu.setPassword(password);
        //将user和设备id进行绑定
        bu.setDeviceType("android");
        bu.setInstallId(BmobInstallation.getInstallationId(this));
        bu.signUp(RegisterActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                progress.dismiss();
                showToast("注册成功");
                // 将设备与username进行绑定
                userManager.bindInstallationForRegister(bu.getUsername());
                //发广播通知登陆页面退出
                sendBroadcast(new Intent(Config.ACTION_REGISTER_SUCCESS_FINISH));
                // 启动主页
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                BmobLog.i(arg1);
                showToast("注册失败:" + arg1);
                progress.dismiss();
            }
        });
    }
}
