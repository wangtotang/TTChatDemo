package com.wangtotang.ttchatdemo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bmob.utils.BmobLog;
import com.wangtotang.ttchatdemo.manager.CustomApplication;
import com.wangtotang.ttchatdemo.ui.R;

/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class BaseFragment extends Fragment {

    /**
     * 公用的Header布局
     */
    public HeaderLayout mHeaderLayout;
    public CustomApplication mApplication;
    public LayoutInflater mInflater;
    protected View contentView;
    private Handler handler = new Handler();
    private Toast mToast;

    public BaseFragment() {

    }

    public void runOnWorkThread(Runnable action) {
        new Thread(action).start();
    }

    public void runOnUiThread(Runnable action) {
        handler.post(action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mApplication = CustomApplication.getInstance();
        mInflater = LayoutInflater.from(getActivity());
    }


    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showToast(int text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


    /**
     * 打Log
     * ShowLog
     *
     * @return void
     * @throws
     */
    public void showLog(String msg) {
        BmobLog.i(msg);
    }

    public View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

    /**
     * 只有title initTopBarLayoutByTitle
     *
     * @throws
     * @Title: initTopBarLayoutByTitle
     */
    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    /**
     * 初始化标题栏-带左右按钮
     *
     * @return void
     * @throws
     */
    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 只有左边按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    /**
     * 右边+title
     * initTopBarForRight
     *
     * @return void
     * @throws
     */
    public void initTopBarForRight(String titleName, int rightDrawableId,
                                   HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    // 左边按钮的点击事件
    public class OnLeftButtonClickListener implements
            HeaderLayout.onLeftImageButtonClickListener {
        @Override
        public void onClick() {
            getActivity().finish();
        }
    }

    /**
     * 动画启动页面 startAnimActivity
     *
     * @throws
     */
    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
    }

    public void startAnimActivity(Class<?> cla) {
        getActivity().startActivity(new Intent(getActivity(), cla));
    }
}
