package com.wangtotang.ttchatdemo.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Wangto Tang on 2015/3/28.
 */
public class User extends BmobUser{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String avatar;
    /**
     * //显示数据拼音的首字母
     */
    private String sortLetters;

    /**
     * //性别-true-男
     */
    private boolean sex;

    /**
     * 地理坐标
     */
    private BmobGeoPoint location;//

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}