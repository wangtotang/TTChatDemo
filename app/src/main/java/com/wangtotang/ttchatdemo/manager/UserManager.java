package com.wangtotang.ttchatdemo.manager;


/**
 * Created by Wangto Tang on 2015/3/29.
 */
public class UserManager{

   private static UserManager userManager = new UserManager();
   private UserManager(){

   }
   public static UserManager getInstance(){
        return userManager;
   }
   public Object getCurrentUser(Class cls){
       try {
           return cls.newInstance();
       } catch (Exception e) {
           e.printStackTrace();
       }
          return null;
   }
}
