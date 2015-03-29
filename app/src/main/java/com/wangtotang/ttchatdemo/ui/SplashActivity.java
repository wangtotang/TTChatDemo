package com.wangtotang.ttchatdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.wangtotang.ttchatdemo.config.Config;

import cn.bmob.v3.Bmob;

/**
 * Created by Wangto Tang on 2015/3/26.
 */
public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this,R.layout.activity_splash,null);
        setContentView(view);
        Bmob.initialize(this, Config.applicationId);
        AlphaAnimation alphaAnim = new AlphaAnimation(0.3f,1.0f);
        alphaAnim.setDuration(2000);
        view.startAnimation(alphaAnim);
        alphaAnim.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}

        });
    }

    private void redirectTo() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
