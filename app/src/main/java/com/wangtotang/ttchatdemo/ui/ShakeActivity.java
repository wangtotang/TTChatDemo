package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;

import com.wangtotang.ttchatdemo.R;

/**
 * Created by Wangto Tang on 2015/4/2.
 */
public class ShakeActivity extends CheckActivity{

    String from = "";

    private double QUERY_KILOMETERS = 1;//默认查询10公里范围内的人
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initView();
    }

    private void initView() {
        initTopBarForLeft("摇一摇");
    }
}
