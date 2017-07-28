package com.zhenai.xunta.presenter.login;

import com.zhenai.xunta.view.ILoginView;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public class LoginPresenter {

    ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    //登录逻辑
    public void doLogin( final String phoneNumber, final String password){
        loginView.toMainActivity();

        /*new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("phoneNumber",phoneNumber)
                        .add("password",password)
                        .build();

                Request request = new Request.Builder()
                        .url("xxx")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response != null){
                        String responseData = response.body().toString();

                        //解析返回的Json
                        int stateCode = 1;//返回登录状态码
                        if (1 == stateCode){
                            loginView.toMainActivity();
                        }else{
                            loginView.showLoginError();
                            //ShowToast.showToast("账号或密码有误，请稍后重试");
                        }
                    }else{
                        loginView.showLoginError();
                        //ShowToast.showToast("服务器异常，请稍后重试");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();*/


    }


}
