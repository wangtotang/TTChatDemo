package com.wangtotang.ttchatdemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangtotang.ttchatdemo.ui.R;
import com.wangtotang.ttchatdemo.util.ImageLoadOptions;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.PushListener;

/**
 * Created by Wangto Tang on 2015/3/31.
 */
public class AddFriendAdapter extends BaseListAdapter<BmobChatUser> {

    public AddFriendAdapter(Context context, List<BmobChatUser> list) {
        super(context, list);
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobChatUser contract = getList().get(arg0);
        TextView name = ViewHolder.get(convertView, R.id.name);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.avatar);

        Button btn_add = ViewHolder.get(convertView, R.id.btn_add);

        String avatar = contract.getAvatar();

        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
        } else {
            iv_avatar.setImageResource(R.drawable.default_head);
        }

        name.setText(contract.getUsername());
        btn_add.setText("添加");
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final ProgressDialog progress = new ProgressDialog(mContext);
                progress.setMessage("正在添加...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                //发送tag请求
                BmobChatManager.getInstance(mContext).sendTagMessage(BmobConfig.TAG_ADD_CONTACT, contract.getObjectId(),new PushListener() {

                    @Override
                    public void onSuccess() {
                        progress.dismiss();
                        showToast("发送请求成功，等待对方验证!");
                    }

                    @Override
                    public void onFailure(int arg0, final String arg1) {
                        progress.dismiss();
                        showToast("发送请求失败，请重新添加!");
                        ShowLog("发送请求失败:"+arg1);
                    }
                });
            }
        });
        return convertView;
    }

}
