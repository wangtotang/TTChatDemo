package com.wangtotang.ttchatdemo.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.bean.User;
import com.wangtotang.ttchatdemo.ui.view.HeaderLayout;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Wangto Tang on 2015/3/30.
 */
public class UpdateInfoActivity extends BaseActivity {
    EditText edit_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_updateinfo);
        initView();
    }

    private void initView() {
        initTopBarForBoth("修改昵称", R.drawable.base_action_bar_true_bg_selector,
                new HeaderLayout.onRightImageButtonClickListener() {

                    @Override
                    public void onClick() {
                        String nick = edit_nick.getText().toString();
                        if (nick.equals("")) {
                            showToast("请填写昵称!");
                            return;
                        }
                        updateInfo(nick);
                    }
                });
        edit_nick = (EditText) findViewById(R.id.edit_nick);
    }

    /** 修改资料
     * updateInfo
     * @Title: updateInfo
     * @return void
     * @throws
     */
    private void updateInfo(String nick) {
        final User user = (User)userManager.getCurrentUser(User.class);
        user.setNick(nick);
        user.update(UpdateInfoActivity.this, new UpdateListener() {

            @Override
            public void onSuccess() {
                showToast("修改成功");
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                showToast("onFailure:" + arg1);
            }
        });
    }
}
