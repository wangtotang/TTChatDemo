package com.wangtotang.ttchatdemo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangtotang.ttchatdemo.manager.CustomApplication;
import com.wangtotang.ttchatdemo.ui.LoginActivity;
import com.wangtotang.ttchatdemo.ui.R;
import com.wangtotang.ttchatdemo.ui.SetMyInfoActivity;

/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    Button btn_logout;
    TextView tv_set_name;
    RelativeLayout layout_info, rl_switch_notification, rl_switch_voice,
            rl_switch_vibrate,layout_blacklist;

    ImageView iv_open_notification, iv_close_notification, iv_open_voice,
            iv_close_voice, iv_open_vibrate, iv_close_vibrate;

    View view1,view2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initTopBarForOnlyTitle("设置");
        //黑名单列表
        layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);

        layout_info = (RelativeLayout) findViewById(R.id.layout_info);
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
        rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_voice.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);

        iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
        iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
        iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
        iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
        iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
        iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);
        view1 = (View) findViewById(R.id.view1);
        view2 = (View) findViewById(R.id.view2);

        tv_set_name = (TextView) findViewById(R.id.tv_set_name);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
        layout_info.setOnClickListener(this);
        layout_blacklist.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_info:// 启动到个人资料页面
                Intent intent =new Intent(getActivity(),SetMyInfoActivity.class);
                intent.putExtra("from", "me");
                startActivity(intent);
                break;
            case R.id.btn_logout:
                CustomApplication.getInstance().logout();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }
}
