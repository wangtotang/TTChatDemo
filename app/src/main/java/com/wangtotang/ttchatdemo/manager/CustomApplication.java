package com.wangtotang.ttchatdemo.manager;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.util.CollectionUtil;
import com.wangtotang.ttchatdemo.util.SharePreferenceUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;

/**
 * Created by Wangto Tang on 2015/3/28.
 */
public class CustomApplication extends Application {
    public static CustomApplication mInstance;
    MediaPlayer mMediaPlayer;
    NotificationManager mNotificationManager;
    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        initImageLoader(getApplicationContext());
        // 若用户登陆过，则先从好友数据库中取出好友list存入内存中
        if (BmobUserManager.getInstance(getApplicationContext())
                .getCurrentUser() != null) {
            // 获取本地好友user list到内存,方便以后获取好友list
            contactList = CollectionUtil.list2map(BmobDB.create(getApplicationContext()).getContactList());
        }
        initBaidu();
    }

    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ttChat/cache");// 获取到缓存的目录地址
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    public static CustomApplication getInstance() {
        return mInstance;
    }

    // 单例模式，才能及时返回数据
    SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String currentId = BmobUserManager.getInstance(
                    getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    public synchronized MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        return mMediaPlayer;
    }




    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, BmobChatUser> getContactList() {
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     * @param contactList
     */
    public void setContactList(Map<String, BmobChatUser> contactList) {
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = contactList;
    }

    /**
     * 退出登录,清空缓存数据
     */
    public void logout() {
        BmobUserManager.getInstance(getApplicationContext()).logout();
        setContactList(null);
    }

    /**
     * 初始化百度相关sdk initBaidumap
     * @Title: initBaidumap
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initBaidu() {
        // 初始化地图Sdk
        SDKInitializer.initialize(getApplicationContext());
    }
}
