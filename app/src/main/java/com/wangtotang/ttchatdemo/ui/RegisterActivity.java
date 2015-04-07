package com.wangtotang.ttchatdemo.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.util.CommonUtil;
import com.wangtotang.ttchatdemo.util.PhotoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Wangto Tang on 2015/3/27.
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_nick;
    private EditText et_username;
    private EditText et_password;
    private EditText et_pwd_again;
    private Button btn_register;
    private RadioGroup rg_gender;
    private ImageView iv_avatar;
    String gender = "女";
    LinearLayout layout_register;
    String path;
    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTopBarForLeft("注册");

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)findViewById(checkedId);
               //更新文本内容，以符合选中项
               gender = rb.getText().toString();
            }
        });

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarPop();
            }
        });
        layout_register = (LinearLayout) findViewById(R.id.layout_register);
        et_nick = (EditText) findViewById(R.id.et_nick);
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

        String nick = et_nick.getText().toString();
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_pwd_again.getText().toString();

        if (TextUtils.isEmpty(nick)) {
            showToast(R.string.toast_error_nick_null);
            return;
        }

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
        //注册的时候需要注意两点：1、User表中绑定设备id和type，2、设备表中绑定username字段
        final User bu = new User();
        if(url!=null) {
            bu.setAvatar(url);
        }
        bu.setSex(!"女".equals(gender));
        bu.setNick(nick);
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
                showToast("注册失败:" + arg1);
                progress.dismiss();
            }
        });
    }

    private void showAvatarPop() {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,
                        Config.REQUESTCODE_UPLOADAVATAR_LOCATION);
            }

            @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
                Uri uri = null;
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        showToast("SD不可用");
                        return;
                    }
                    uri = data.getData();
                    startImageAction(uri, 200, 200,
                            Config.REQUESTCODE_UPLOADAVATAR_CROP);
                } else {
                    showToast("照片获取失败");
                }

                break;
            case Config.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
                if (data == null) {
                    return;
                } else {
                    saveCropAvator(data);
                }
                // 上传头像
                uploadAvatar();
                break;
            default:
                break;

        }
    }

    /**
     * @Title: startImageAction
     * @return void
     * @throws
     */
    private void startImageAction(Uri uri, int outputX, int outputY,
                                  int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    /**
     * 保存裁剪的头像
     *
     * @param data
     */
    private void saveCropAvator(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            if (bitmap != null) {
                bitmap = PhotoUtil.toRoundCorner(bitmap,10);
                iv_avatar.setImageBitmap(bitmap);
                // 保存图片
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date())+".jpeg";
                path = Config.MyAvatarDir + filename;
                PhotoUtil.saveBitmap(Config.MyAvatarDir, filename,
                        bitmap, true);
                // 上传头像
                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    private void uploadAvatar() {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(RegisterActivity.this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                url = bmobFile.getFileUrl(getApplicationContext());
            }

            @Override
            public void onProgress(Integer arg0) {

            }

            @Override
            public void onFailure(int arg0, String msg) {
                showToast("头像上传失败：" + msg);
            }
        });
    }

}
