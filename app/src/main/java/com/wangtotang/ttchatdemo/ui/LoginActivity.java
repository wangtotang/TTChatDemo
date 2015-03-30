package com.wangtotang.ttchatdemo.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bmob.utils.BmobLog;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.util.CommonUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Wangto Tang on 2015/3/26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(receiver, filter);

    }

    private void init() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Config.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
                finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                boolean isNetConnected = CommonUtil.isNetworkAvailable(this);
                if(!isNetConnected){
                    showToast(R.string.network_tips);
                    return;
                }
                login();
            break;
            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            break;
        }
    }

    private void login(){
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(username)) {
            showToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(R.string.toast_error_password_null);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登陆...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(LoginActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                progress.dismiss();
                showToast("登陆成功");
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                BmobLog.i(arg1);
                showToast("登陆失败:" + arg1);
                progress.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
