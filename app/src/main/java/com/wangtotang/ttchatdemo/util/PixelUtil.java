package com.wangtotang.ttchatdemo.util;

import android.content.Context;
import android.content.res.Resources;

import com.wangtotang.ttchatdemo.manager.CustomApplication;

/**
 * Created by Wangto Tang on 2015/3/28.
 */
public class PixelUtil {
    /**
     * The context.
     */
    private static Context mContext = CustomApplication.getInstance();

    /**
     * dp转 px.
     *
     * @param value the value
     * @return the int
     */
    public static int dp2px(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * sp转px.
     *
     * @param value the value
     * @return the int
     */
    public static int sp2px(float value) {
        Resources r;
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

}
