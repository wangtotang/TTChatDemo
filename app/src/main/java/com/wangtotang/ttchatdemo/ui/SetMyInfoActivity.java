package com.wangtotang.ttchatdemo.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.util.ImageLoadOptions;
import com.wangtotang.ttchatdemo.util.PhotoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class SetMyInfoActivity extends CheckActivity implements View.OnClickListener {

    TextView  tv_set_nick, tv_set_gender;
    ImageView iv_set_avator, iv_arraw, iv_nickarraw,iv_genderarraw;
    LinearLayout layout_all;

    Button btn_chat, btn_add_friend;
    RelativeLayout layout_head, layout_nick, layout_gender;
    String path;
    String from = "";
    String username = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);
        from = getIntent().getStringExtra("from");//me add other
        username = getIntent().getStringExtra("username");
        initView();
    }

    private void initView() {
        layout_all = (LinearLayout) findViewById(R.id.layout_all);
        iv_set_avator = (ImageView) findViewById(R.id.iv_set_avator);
        iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
        iv_nickarraw = (ImageView) findViewById(R.id.iv_nickarraw);
        iv_genderarraw = (ImageView) findViewById(R.id.iv_genderarraw);
        tv_set_nick = (TextView) findViewById(R.id.tv_set_nick);
        layout_head = (RelativeLayout) findViewById(R.id.layout_head);
        layout_nick = (RelativeLayout) findViewById(R.id.layout_nick);
        layout_gender = (RelativeLayout) findViewById(R.id.layout_gender);
        tv_set_gender = (TextView) findViewById(R.id.tv_set_gender);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_add_friend.setEnabled(false);
        btn_chat.setEnabled(false);
        if (from.equals("me")) {
            initTopBarForLeft("个人资料");
            layout_head.setOnClickListener(this);
            layout_nick.setOnClickListener(this);
            layout_gender.setOnClickListener(this);
            iv_nickarraw.setVisibility(View.VISIBLE);
            iv_arraw.setVisibility(View.VISIBLE);
            iv_genderarraw.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.GONE);
            btn_add_friend.setVisibility(View.GONE);
        } else {
            initTopBarForLeft("详细资料");
            iv_nickarraw.setVisibility(View.INVISIBLE);
            iv_arraw.setVisibility(View.INVISIBLE);
            iv_genderarraw.setVisibility(View.INVISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
            btn_chat.setOnClickListener(this);
            if (from.equals("add")) {// 从附近的人列表添加好友--因为获取附近的人的方法里面有是否显示好友的情况，因此在这里需要判断下这个用户是否是自己的好友
                if (mApplication.getContactList().containsKey(username)) {// 是好友
                } else {
                    btn_add_friend.setVisibility(View.VISIBLE);
                    btn_add_friend.setOnClickListener(this);
                }
            }
            initOtherData(username);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat:// 发起聊天
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("user", user);
                startAnimActivity(intent);
                finish();
                break;
            case R.id.layout_head:
                showAvatarPop();
                break;
            case R.id.layout_nick:
                startAnimActivity(UpdateInfoActivity.class);
                break;
            case R.id.layout_gender:// 性别
                showSexChooseDialog();
                break;
            case R.id.btn_add_friend://添加好友
                addFriend();
                break;
        }
    }

    private void initMeData() {
        User user = userManager.getCurrentUser(User.class);
        initOtherData(user.getUsername());
    }


    private void initOtherData(String name) {
        userManager.queryUser(name, new FindListener<User>() {

            @Override
            public void onError(int arg0, String arg1) {
                showLog("onError onError:" + arg1);
            }

            @Override
            public void onSuccess(List<User> arg0) {
                if (arg0 != null && arg0.size() > 0) {
                    user = arg0.get(0);
                    updateUser(user);
                    btn_chat.setEnabled(true);
                    btn_add_friend.setEnabled(true);
                } else {
                    showLog("onSuccess 查无此人");
                }
            }
        });
    }

    private void updateUser(User user) {
        // 更改
        refreshAvatar(user.getAvatar());
        tv_set_nick.setText(user.getNick());
        tv_set_gender.setText(user.getSex() == true ? "男" : "女");
    }

    /**
     * 更新头像 refreshAvatar
     *
     * @return void
     * @throws
     */
    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_set_avator,
                    ImageLoadOptions.getOptions());
        } else {
            iv_set_avator.setImageResource(R.drawable.default_head);
        }
    }



    private void showAvatarPop() {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,
                       Config.REQUESTCODE_UPLOADAVATAR_LOCATION);
            }

    /**
     * @Title: startImageAction
     * @return void
     * @throws
     */
    private void startImageAction(Uri uri, int outputX, int outputY,
                                  int requestCode, boolean isCrop) {
        Intent intent = null;
        if (isCrop) {
            intent = new Intent("com.android.camera.action.CROP");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        }
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
                            Config.REQUESTCODE_UPLOADAVATAR_CROP, true);
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

    private void uploadAvatar() {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(SetMyInfoActivity.this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                String url = bmobFile.getFileUrl(getApplicationContext());
                // 更新BmobUser对象
                updateUserAvatar(url);
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

    private void updateUserAvatar(final String url) {
        User  u =new User();
        u.setAvatar(url);
        updateUserData(u,new UpdateListener() {
            @Override
            public void onSuccess() {
                showToast("头像更新成功！");
                // 更新头像
                refreshAvatar(url);
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast("头像更新失败：" + msg);
            }
        });
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
            Log.i("life", "avatar - bitmap = " + bitmap);
            if (bitmap != null) {
                bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
                iv_set_avator.setImageBitmap(bitmap);
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

    private void updateUserData(User user,UpdateListener listener){
        User current = userManager.getCurrentUser(User.class);
        user.setObjectId(current.getObjectId());
        user.update(this, listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals("me")) {
            initMeData();
        }
    }

    Map<String,String> map1 = new HashMap<String,String>(){
        {
            put("gender","男");
        }
    };
    Map<String,String> map2 = new HashMap<String,String>(){
        {
            put("gender","女");
        }
    };
    List list = new ArrayList(){
        {
            add(map1);
            add(map2);
        }
    };
    private void showSexChooseDialog() {
        SimpleAdapter listAdapter = new SimpleAdapter(this,list,R.layout.item_gender,new String[]{"gender"},new int[]{R.id.tv_gender});
        new AlertDialog.Builder(this)
                .setTitle("性别")
                .setIcon(R.drawable.icon_gender)
                .setSingleChoiceItems(listAdapter, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateInfo(which);
                                dialog.dismiss();
                            }
                        })
                .show();
    }


    /** 修改资料
     * updateInfo
     * @Title: updateInfo
     * @return void
     * @throws
     */
    private void updateInfo(int which) {
        final User u = new User();
        if(which==0){
            u.setSex(true);
        }else{
            u.setSex(false);
        }
        updateUserData(u,new UpdateListener() {

            @Override
            public void onSuccess() {
                showToast("修改成功");
                tv_set_gender.setText(u.getSex() == true ? "男" : "女");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                showToast("onFailure:" + arg1);
            }
        });
    }

    /**
     * 添加好友请求
     *
     * @Title: addFriend
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void addFriend() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("正在添加...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        // 发送tag请求
        BmobChatManager.getInstance(this).sendTagMessage(BmobConfig.TAG_ADD_AGREE,
                user.getObjectId(), new PushListener() {

                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                       BmobDB.create(SetMyInfoActivity.this).saveContact(user);
                    }

                    @Override
                    public void onFailure(int arg0, final String arg1) {
                        progress.dismiss();
                        showLog("发送请求失败:" + arg1);
                    }
                });
    }

}
