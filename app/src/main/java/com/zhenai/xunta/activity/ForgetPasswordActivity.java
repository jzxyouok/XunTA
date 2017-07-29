package com.zhenai.xunta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.HttpUtil;
import com.zhenai.xunta.utils.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 忘记密码Activity
 * Created by wenjing.tang on 2017/7/26.
 */

public class ForgetPasswordActivity extends Activity implements View.OnClickListener{

    private EditText mEtPhoneNumber, mEtValidateCode, mEtPassword;
    private Button mBtnSendValidateCode, mBtnNext;

    private int btnClickedCount = 0; //记录发送验证码Button点击的次数
    String phoneNumber;
    String validateCodeFromServer ; //服务器返回的验证码

    private TimeCount mTimeCount;//计时器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);

        initViews();

        setListeners();
    }

    private void initViews() {
        mEtPhoneNumber = findViewById(R.id.et_forgetpassword_telphone_number);
        mEtValidateCode = findViewById(R.id.et_forgetpassword_validate_code);
        mEtPassword = findViewById(R.id.et_forgetpassword_password);

        mBtnSendValidateCode = findViewById(R.id.btn_forgetpassword__validate_code);
        mBtnNext = findViewById(R.id.btn_forget_password_query);

        mTimeCount = new TimeCount(60000, 1000);
    }

    private void setListeners() {
        mBtnSendValidateCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_forgetpassword__validate_code:
                phoneNumber = mEtPhoneNumber.getText().toString(); //获取输入的手机号
                if (phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$") && (btnClickedCount <=3 )) {

                    HttpUtil.sendPostRequestWithOkHttp("http://10.1.3.39:8080/login","phone", phoneNumber, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ShowToast.showToast("网络异常，请稍后再试~");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                           String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                 validateCodeFromServer = jsonObject.getString("resultCode");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    mTimeCount.start();
                    btnClickedCount++;
                    // Log.e("tag", "btnClickedCount:" +btnClickedCount + "");
                }else if(btnClickedCount > 3){
                    ShowToast.showToast("操作太频繁，请明天再试~");
                }else {
                    ShowToast.showToast("请填写有效手机号~");
                }
                break;

            case R.id.btn_forget_password_query:
                String validateCodeInput = mEtValidateCode.getText().toString(); //获取输入的验证码
                String password = mEtPassword.getText().toString(); //获取输入的密码
                String regex = "[0-9A-Za-z]{6,20}";//6-20位数字或者字母
                if(validateCodeInput.length() == 0 || (!validateCodeInput.equals(validateCodeFromServer))){
                    ShowToast.showToast("验证码不正确，请重新输入验证码~");
                    Log.e("tag",validateCodeInput +":" + validateCodeFromServer);
                }else if(!password.matches(regex)) {
                    ShowToast.showToast("密码只能由6-20位数字或英文组成~");
                }else {
                    Intent intent = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mBtnSendValidateCode.setClickable(false);
            mBtnSendValidateCode.setText(l/1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            mBtnSendValidateCode.setClickable(true);
            mBtnSendValidateCode.setText("获取验证码");
        }
    }
}
