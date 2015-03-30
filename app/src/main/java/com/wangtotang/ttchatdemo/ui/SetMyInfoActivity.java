package com.wangtotang.ttchatdemo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.utils.BmobLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.config.Config;
import com.wangtotang.ttchatdemo.util.ImageLoadOptions;
import com.wangtotang.ttchatdemo.util.PhotoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class SetMyInfoActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_set_name, tv_set_nick, tv_set_gender;
    ImageView iv_set_avator, iv_arraw, iv_nickarraw;
    LinearLayout layout_all;

    Button btn_chat, btn_back, btn_add_friend;
    RelativeLayout layout_head, layout_nick, layout_gender, layout_black_tips;
    String path;
    String from = "";
    String username = "";
    RelativeLayout layout_choose;
    RelativeLayout layout_photo;
    PopupWindow avatorPop;
    public String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 因为魅族手机下面有三个虚拟的导航按钮，需要将其隐藏掉，不然会遮掉拍照和相册两个按钮，且在setContentView之前调用才能生效
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
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
        tv_set_name = (TextView) findViewById(R.id.tv_set_name);
        tv_set_nick = (TextView) findViewById(R.id.tv_set_nick);
        layout_head = (RelativeLayout) findViewById(R.id.layout_head);
        layout_nick = (RelativeLayout) findViewById(R.id.layout_nick);
        layout_gender = (RelativeLayout) findViewById(R.id.layout_gender);
        // 黑名单提示语
        layout_black_tips = (RelativeLayout) findViewById(R.id.layout_black_tips);
        tv_set_gender = (TextView) findViewById(R.id.tv_set_gender);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_add_friend.setEnabled(false);
        btn_chat.setEnabled(false);
        btn_back.setEnabled(false);
        if (from.equals("me")) {
            initTopBarForLeft("个人资料");
            layout_head.setOnClickListener(this);
            layout_nick.setOnClickListener(this);
            layout_gender.setOnClickListener(this);
            iv_nickarraw.setVisibility(View.VISIBLE);
            iv_arraw.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
            btn_add_friend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head:
                showAvatarPop();
                break;
        }
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
        View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator,
                null);
        layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
        layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
        layout_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showLog("点击拍照");
                layout_choose.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_photo.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                File dir = new File(Config.MyAvatarDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // 原图
                File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date()));
                filePath = file.getAbsolutePath();// 获取相片的保存路径
                Uri imageUri = Uri.fromFile(file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,
                        Config.REQUESTCODE_UPLOADAVATAR_CAMERA);
            }
        });
        layout_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showLog("点击相册");
                layout_photo.setBackgroundColor(getResources().getColor(
                        R.color.base_color_text_white));
                layout_choose.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.pop_bg_press));
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,
                        Config.REQUESTCODE_UPLOADAVATAR_LOCATION);
            }
        });

        avatorPop = new PopupWindow(view, mScreenWidth, 600);
        avatorPop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    avatorPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        avatorPop.setTouchable(true);
        avatorPop.setFocusable(true);
        avatorPop.setOutsideTouchable(true);
        avatorPop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
        avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
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

    Bitmap newBitmap;
    boolean isFromCamera = false;// 区分拍照旋转
    int degree = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        showToast("SD不可用");
                        return;
                    }
                    isFromCamera = true;
                    File file = new File(filePath);
                    degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
                    Log.i("life", "拍照后的角度：" + degree);
                    startImageAction(Uri.fromFile(file), 200, 200,
                            Config.REQUESTCODE_UPLOADAVATAR_CROP, true);
                }
                break;
            case Config.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
                if (avatorPop != null) {
                    avatorPop.dismiss();
                }
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
                    isFromCamera = false;
                    uri = data.getData();
                    startImageAction(uri, 200, 200,
                            Config.REQUESTCODE_UPLOADAVATAR_CROP, true);
                } else {
                    showToast("照片获取失败");
                }

                break;
            case Config.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
                // TODO sent to crop
                if (avatorPop != null) {
                    avatorPop.dismiss();
                }
                if (data == null) {
                    // Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveCropAvator(data);
                }
                // 初始化文件路径
                filePath = "";
                // 上传头像
                uploadAvatar();
                break;
            default:
                break;

        }
    }

    private void uploadAvatar() {
        BmobLog.i("头像地址：" + path);
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(this, new UploadFileListener() {

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
        User user = (User) userManager.getCurrentUser(User.class);
        user.setAvatar(url);
        user.update(this, new UpdateListener() {
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
                if (isFromCamera && degree != 0) {
                    bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
                }
                iv_set_avator.setImageBitmap(bitmap);
                // 保存图片
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date());
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
}
