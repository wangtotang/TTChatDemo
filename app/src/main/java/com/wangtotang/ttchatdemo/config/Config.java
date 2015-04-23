package com.wangtotang.ttchatdemo.config;

/**
 * Created by Wangto Tang on 2015/3/26.
 */
public class Config {

    public static String applicationId = "5bc8e0abb17c69b202e203ae58cfe23d";
    /**
     * 我的语音保存目录
     */
    public static String MyVoiceDir = "/sdcard/ttChat/voice";
    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/ttChat/avatar/";
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出
    //游戏规模
    public static int SIZE = 3;
    public static int panelBG = 0xffffffff;
    //表示四个移动方向
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int UNMOVABLE = -1;

}
