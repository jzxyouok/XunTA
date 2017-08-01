package com.zhenai.xunta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.SharedPreferencesUtil;

/**
 * 启动页
 * Created by wenjing.tang on 2017/7/31.
 */

public class SplashActivity extends BaseActivity {

    //public static final boolean isRegister = false;
   // public static final boolean isLogin = false;

    private int isRegisterFlag = 0; //0：未注册；1：已注册
    private int isLoginFlag = 0;//0：未登录；1：已登录
    public static final int SPLASH_DURATION_TIME = 1500;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //arg1：注册标识；arg2：登录标识
           /* if (msg.arg1 == 0 && msg.arg2 == 0){
                startActivity(new Intent(SplashActivity.this, LaunchActivity.class));
                finish();
            }else if(msg.arg1 == 1 && msg.arg2 == 0){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }else if(msg.arg1 == 1 && msg.arg2 == 1){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }*/
            if(msg.arg2 == 0){
                startActivity(new Intent(SplashActivity.this, LaunchActivity.class));
                finish();
            }else if(msg.arg2 == 1){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //从SharedPreferences取账号
        String phone =(String) SharedPreferencesUtil.getParam(SplashActivity.this, "phone", "");

        if ( phone!= null && !phone.equals("")){
            isLoginFlag = 1;
        }
/*        HttpUtil.sendPostRequestWithOkHttp("http://10.1.3.39:8080/login", "phone", "15674558744", new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if(e.getCause().equals(SocketTimeoutException.class) )//超时捕获。如果请求超时，默认进入登录页面
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response != null){
                    String responseData = response.body().string();
                    Log.e("json",responseData);

                    //解析json，模拟后台返回
                    isRegisterFlag = 0;
                }
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Message message = new Message();
                message.arg1 = isRegisterFlag;
                message.arg2 = isLoginFlag;
                handler.sendMessage(message);
            }
        }, SPLASH_DURATION_TIME);

    }

}
