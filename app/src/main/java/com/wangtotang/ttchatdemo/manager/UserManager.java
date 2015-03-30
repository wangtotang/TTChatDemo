package com.wangtotang.ttchatdemo.manager;


import android.content.Context;
import com.wangtotang.ttchatdemo.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class UserManager extends BmobQuery{
   private static Context context = null;
   private static UserManager userManager = null;

   private UserManager(){
   }

   public static UserManager getInstance(Context mContext){
       if(userManager==null) {
           userManager = new UserManager();
           context = mContext;
           return userManager;
       }
       return userManager;
   }

   public Object getCurrentUser(Class cls){
       return User.getCurrentUser(context,cls);
   }

    public Object getCurrentUser(){
        return User.getCurrentUser(context);
    }

    public void queryUser(String username,FindListener<User> findListener){
        BmobQuery<User> query = new BmobQuery<>();
        query.findObjects(context,findListener);
    }
}
