package com.zhenai.xunta.model;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public class User {

    private int id; //用户ID
    private String nickname; //昵称
    private String avatar; //头像
    private String sex; //性别
    private String phone; //手机号
    private String password; //账号密码
    private String province; //省份
    private String city; //市
    private String district; //区
    private String marriageStatus;//感情状况
    private String profession; //职业
    private String isSmoking;//是否吸烟
    private String isDrinking;//是否喝酒
    private String skills;//技能
    private String hobby;//兴趣爱好

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getIsSmoking() {
        return isSmoking;
    }

    public void setIsSmoking(String isSmoking) {
        this.isSmoking = isSmoking;
    }

    public String getIsDrinking() {
        return isDrinking;
    }

    public void setIsDrinking(String isDrinking) {
        this.isDrinking = isDrinking;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String telphone) {
        this.phone = telphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
