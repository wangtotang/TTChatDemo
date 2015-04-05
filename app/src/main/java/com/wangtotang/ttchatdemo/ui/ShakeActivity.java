package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.MapView;
import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.listener.ShakeListener;
import com.wangtotang.ttchatdemo.manager.MapManager;

/**
 * Created by Wangto Tang on 2015/4/2.
 */
public class ShakeActivity extends CheckActivity{

    Vibrator mVibrator;
    RelativeLayout mImgUp;
    RelativeLayout mImgDn;
    ShakeListener mShakeListener = null;
    MapView mMapView = null;
    MapManager mapManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initView();
        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                startAnim();  //开始 摇一摇手掌动画
                mShakeListener.stop();
                startVibrato(); //开始 震动
                mMapView.setVisibility(View.VISIBLE);
                mapManager = new MapManager(mMapView,ShakeActivity.this);
                mapManager.initBaiduLocClient();
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        mVibrator.cancel();
                        //mShakeListener.start();
                    }
                }, 2000);
            }
        });

    }

    private void initView() {
        initTopBarForLeft("摇一摇");
        mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
        mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
        mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
        mMapView = (MapView) findViewById(R.id.mapView);
    }

    public void startAnim () {   //定义摇一摇动画动画
        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f);
        mytranslateanimup0.setDuration(1000);
        //TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
        //mytranslateanimup1.setDuration(1000);
        //mytranslateanimup1.setStartOffset(1000);
        animup.addAnimation(mytranslateanimup0);
        //animup.addAnimation(mytranslateanimup1);
        mImgUp.startAnimation(animup);
        mImgUp.setVisibility(View.GONE);


        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+1.0f);
        mytranslateanimdn0.setDuration(1000);
        //TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f);
        //mytranslateanimdn1.setDuration(1000);
        //mytranslateanimdn1.setStartOffset(1000);
        animdn.addAnimation(mytranslateanimdn0);
        //animdn.addAnimation(mytranslateanimdn1);
        mImgDn.startAnimation(animdn);
        mImgDn.setVisibility(View.GONE);

    }

    public void startVibrato(){		//定义震动
        mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1从pattern的指定下标开始重复
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShakeListener != null) {
            mShakeListener.stop();
        }
        if(mapManager!=null) {
            mapManager.cancel();
            mMapView.onDestroy();
            mMapView = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
