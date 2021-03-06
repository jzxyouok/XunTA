package com.zhenai.xunta.utils;

/**
 * 服务器URL类
 * Created by wenjing.tang on 2017/8/2.
 */

public  class ServerUrl {

    // 登录
    public static final String LOGIN_URL = "http://10.1.3.33:8080/login";


    //注册发送短信
    public static final String SEND_MESSAGE_URL = "http://10.1.3.33:8080/register/sendMessageCode";

    //校验验证码接口
    public static final String CHECK_MESSAGE_URL = "http://10.1.3.33:8080/register/checkMessageCode";

    // 注册
    public static final String REGISTER_URL = "http://10.1.3.33:8080/register";

    //图片
    public static final String UPLOAD_PHOTO_URL = "http://10.1.3.33:8080/photo/uploadMultiPhoto";

    //退出
    public static final String LOG_OUT_URL = "http://10.1.3.33:8080/logout";

    //颜值评分
    public static final String FACE_SCORE_URL = "http://10.1.3.33:8080/faceAuth/getFaceScore";


}
