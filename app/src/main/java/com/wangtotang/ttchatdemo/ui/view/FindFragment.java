package com.wangtotang.ttchatdemo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wangtotang.ttchatdemo.R;
import com.wangtotang.ttchatdemo.ui.ShakeActivity;

/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class FindFragment extends BaseFragment {
    LinearLayout layout_near;//附近的人
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initTopBarForOnlyTitle("发现");
        layout_near =(LinearLayout)findViewById(R.id.layout_near);
        layout_near.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ShakeActivity.class);
                startAnimActivity(intent);
            }
        });
    }

}
