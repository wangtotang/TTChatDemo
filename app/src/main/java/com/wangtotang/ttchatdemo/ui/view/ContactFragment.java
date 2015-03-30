package com.wangtotang.ttchatdemo.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wangtotang.ttchatdemo.ui.R;

/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class ContactFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

}
