package com.zhenai.xunta.model;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public class User {

    private int id; //用户ID
    private String nickname; //昵称
    private String avatar; //头像
    private String sex; //性别
    private String telphone; //手机号
    private String password; //账号密码
    private String province; //省份
    private String city; //市
    private String district; //区

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
