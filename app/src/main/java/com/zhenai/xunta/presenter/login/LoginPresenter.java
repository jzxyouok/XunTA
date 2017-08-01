package com.zhenai.xunta.presenter.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.zhenai.xunta.activity.RegisterActivity;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
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
    private Context mContext;

    public static final String LOGIN_SUCCESS = "1701021";
    public static final String LOGIN_FAILURE = "1701022";
    public static  final String PHONE_IS_NOT_EXIST = "1701023";
    public static  final String LOGIN_URL = "http://10.1.3.39:8080/login";

    public LoginPresenter(ILoginView loginView, Context mContext) {
        this.loginView = loginView;
        this.mContext = mContext;
    }

    private Handler handler  = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_TOAST_ERROR:
                    loginView.showLoginError();
                   // ShowToast.showToast("账号或密码有误，请重试~");
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
                        .url(LOGIN_URL)
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
                        //对返回码进行判断
                        if (resultCode.equals(LOGIN_SUCCESS)){//登录成功，跳转到主页面，并在SharedPreferences中写入手机号
                            loginView.toMainActivity();
                            SharedPreferencesUtil.setParam(mContext,"phone",phoneNumber);//登录成功，将手机号写入本地
                        }else if(resultCode.equals(PHONE_IS_NOT_EXIST)){ //账号不存在
                            showDialog();
                        }else {
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("您还未注册手机号");
        builder.setMessage("点击确定进行注册");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                mContext.startActivity(intent);
            }
        });
        builder.show();
    }


}
