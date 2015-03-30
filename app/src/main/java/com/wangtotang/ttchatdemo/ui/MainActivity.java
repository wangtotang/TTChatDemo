package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wangtotang.ttchatdemo.ui.view.ContactFragment;
import com.wangtotang.ttchatdemo.ui.view.FindFragment;
import com.wangtotang.ttchatdemo.ui.view.RecentFragment;
import com.wangtotang.ttchatdemo.ui.view.SettingFragment;


public class MainActivity extends BaseActivity {

    private Button[] mTabs;
    private RecentFragment recentFragment;
    private ContactFragment contactFragment;
    private FindFragment findFragment;
    private SettingFragment settingFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private ImageView iv_recent_tips,iv_contact_tips;//消息提示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTab();
    }

    private void initView(){
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_message);
        mTabs[1] = (Button) findViewById(R.id.btn_contract);
        mTabs[2] = (Button) findViewById(R.id.btn_find);
        mTabs[3] = (Button) findViewById(R.id.btn_set);
        iv_recent_tips = (ImageView)findViewById(R.id.iv_recent_tips);
        iv_contact_tips = (ImageView)findViewById(R.id.iv_contact_tips);
        //把第一个tab设为选中状态
        mTabs[0].setSelected(true);
    }

    private void initTab(){
        recentFragment = new RecentFragment();
        contactFragment = new ContactFragment();
        findFragment = new FindFragment();
        settingFragment = new SettingFragment();
        fragments = new Fragment[] {recentFragment, contactFragment, findFragment,settingFragment };
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment).
                add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment).commit();
    }



    /**
     * button点击事件
     * @param view
     */
    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_message:
                index = 0;
                break;
            case R.id.btn_contract:
                index = 1;
                break;
            case R.id.btn_find:
                index = 2;
                break;
            case R.id.btn_set:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        //把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private static long firstTime;
    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            showToast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }
}
