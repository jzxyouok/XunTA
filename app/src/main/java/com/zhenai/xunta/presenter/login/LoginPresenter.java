package com.zhenai.xunta.presenter.login;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.view.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public class LoginPresenter {

    ILoginView loginView;
    private static  final int SHOW_TOAST_ERROR = 1;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    private Handler handler  = new Handler(){

        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_TOAST_ERROR:
                    loginView.showLoginError();
                    ShowToast.showToast("账号或密码有误，请稍后重试");
                    break;
                default:
                    break;
            }
        }
    };

   //登录逻辑
    public void doLogin( final String phoneNumber, final String password){
       // loginView.toMainActivity();

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("phone",phoneNumber)
                        .add("password",password)
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.1.3.39:8080/login")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response != null){
                        String responseData = response.body().string();
                        Log.e("json",responseData);

                        String resultCode="";
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);//解析返回的Json
                            resultCode= jsonObject.getString("resultCode");
                           // Log.e("tag",resultCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (resultCode.equals("200")){
                            loginView.toMainActivity();
                        }else{
                            Message message = new Message();
                            message.what = SHOW_TOAST_ERROR;
                            handler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
