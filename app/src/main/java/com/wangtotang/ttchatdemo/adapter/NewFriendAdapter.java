package com.wangtotang.ttchatdemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangtotang.ttchatdemo.manager.CustomApplication;
import com.wangtotang.ttchatdemo.ui.R;
import com.wangtotang.ttchatdemo.util.CollectionUtil;
import com.wangtotang.ttchatdemo.util.ImageLoadOptions;

import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Wangto Tang on 2015/3/31.
 */
public class NewFriendAdapter extends BaseListAdapter<BmobInvitation> {

    public NewFriendAdapter(Context context, List<BmobInvitation> list) {
        super(context, list);
    }

    @Override
    public View bindView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_add_friend, null);
        }
        final BmobInvitation msg = getList().get(arg0);
        TextView name = ViewHolder.get(convertView, R.id.name);
        ImageView iv_avatar = ViewHolder.get(convertView, R.id.avatar);

        final Button btn_add = ViewHolder.get(convertView, R.id.btn_add);

        String avatar = msg.getAvatar();

        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
        } else {
            iv_avatar.setImageResource(R.drawable.default_head);
        }

        int status = msg.getStatus();
        if(status== BmobConfig.INVITE_ADD_NO_VALIDATION||status==BmobConfig.INVITE_ADD_NO_VALI_RECEIVED){
			btn_add.setText("同意");
			btn_add.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_login_selector));
			btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_white));
            btn_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    BmobLog.i("点击同意按钮:" + msg.getFromid());
                    agressAdd(btn_add, msg);
                }
            });
        }else if(status==BmobConfig.INVITE_ADD_AGREE){
            btn_add.setText("已同意");
            btn_add.setBackgroundDrawable(null);
            btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
            btn_add.setEnabled(false);
        }
        name.setText(msg.getFromname());

        return convertView;
    }


    /**添加好友
     * agressAdd
     * @Title: agressAdd
     * @Description: TODO
     * @param @param btn_add
     * @param @param msg
     * @return void
     * @throws
     */
    private void agressAdd(final Button btn_add,final BmobInvitation msg){
        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage("正在添加...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        try {
            //同意添加好友
            BmobUserManager.getInstance(mContext).agreeAddContact(msg, new UpdateListener() {

                @Override
                public void onSuccess() {
                    progress.dismiss();
                    btn_add.setText("已同意");
                    btn_add.setBackgroundDrawable(null);
                    btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
                    btn_add.setEnabled(false);
                    //保存到application中方便比较
                    CustomApplication.getInstance().setContactList(CollectionUtil.list2map(BmobDB.create(mContext).getContactList()));
                }

                @Override
                public void onFailure(int arg0, final String arg1) {
                    progress.dismiss();
                    showToast("添加失败: " + arg1);
                }
            });
        } catch (final Exception e) {
            progress.dismiss();
            showToast("添加失败: " +e.getMessage());
        }
    }
}
