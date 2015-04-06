package com.wangtotang.ttchatdemo.config;

import android.os.Environment;

/**
 * Created by Wangto Tang on 2015/3/26.
 */
public class Config {
    public static String applicationId = "5bc8e0abb17c69b202e203ae58cfe23d";


    /**
     * 存放发送图片的目录
     */
    public static String BMOB_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/ttchatdemo/image/";

    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/ttchatdemo/avatar/";
    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
    public static final String EXTRA_STRING = "extra_string";


    public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出


    //游戏规模
    public static int SIZE = 3;

    public static int panelBG = 0xff7accc8;
    //方块移动耗时
    public static int MOVE_TIME = 1000;

    //表示四个移动方向
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int UNMOVABLE = -1;

}
