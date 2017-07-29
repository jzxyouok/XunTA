package com.zhenai.xunta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.presenter.login.LoginPresenter;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.view.ILoginView;

/**
 * Created by wenjing.tang on 2017/7/25.
 */

public class LoginActivity  extends Activity implements ILoginView, View.OnClickListener{

    private EditText mPhoneNumberEt, mPasswordEt;
    private Button mLoginBtn;
    private TextView mRegisterTv, mForgetPasswordTv;

    LoginPresenter mLoginPresenter = new LoginPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        setListeners();
    }

    private void initViews() {
        mPhoneNumberEt = findViewById(R.id.et_telphone_number);

        mPasswordEt = findViewById(R.id.et_password);
        //设置输入密码时显示密文
        mPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mLoginBtn = findViewById(R.id.btn_login);
        mRegisterTv = findViewById(R.id.tv_new_user_register);
        mForgetPasswordTv = findViewById(R.id.tv_forget_password);
    }

    private void setListeners() {
        mLoginBtn.setOnClickListener(this);
        mRegisterTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
    }

    @Override
    public void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); //失效的原因：LoginActivity页面finish ，LaunchActivity没有finish。解决
    }

    @Override
    public void showLoginError() {
        ShowToast.showToast("账号或密码有误，请重新输入");
    }

    @Override
    public void toRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void toForgetPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String phoneNumber =mPhoneNumberEt.getText().toString();
                String password = mPasswordEt.getText().toString();
                if (phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$") && (password.length() != 0)) {
                    mLoginPresenter.doLogin(phoneNumber,password);
                }else{
                    ShowToast.showToast("请检查手机号是否正确");
                }
                break;

            case R.id.tv_new_user_register:
                toRegisterActivity();
                break;

            case R.id.tv_forget_password:
                toForgetPasswordActivity();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
