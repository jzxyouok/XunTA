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
import com.zhenai.xunta.utils.ShowToast;

/**
 * 注册页面
 * Created by wenjing.tang on 2017/7/26.
 */

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText mEtPhoneNumber, mEtValidateCode, mEtPassword;
    private Button mBtnSendValidateCode, mBtnNext;

    private int btnClickedCount = 0; //记录发送验证码Button点击的次数
    String phoneNumber;
    String validateCodeFromServer ; //服务器返回的验证码

    private TimeCount mTimeCount;//计时器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initViews();

        setListeners();
    }

    private void initViews() {
        mEtPhoneNumber = findViewById(R.id.et_register_telphone_number);
        mEtValidateCode = findViewById(R.id.et_register_validate_code);
        mEtPassword = findViewById(R.id.et_register_password);

        mBtnSendValidateCode = findViewById(R.id.btn_register_validate_code);
        mBtnNext = findViewById(R.id.btn_register_next);

        mTimeCount = new TimeCount(60000, 1000);
    }

    private void setListeners() {
        mBtnSendValidateCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_register_validate_code:
                phoneNumber = mEtPhoneNumber.getText().toString(); //获取输入的手机号
                if (phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$") && (btnClickedCount <=3 )) {
                    validateCodeFromServer =  httpPostRequest(phoneNumber); //向服务器发Post请求，得到服务器返回的验证码
                    mTimeCount.start();
                    btnClickedCount++;
                   // Log.e("tag", "btnClickedCount:" +btnClickedCount + "");
                }else if(btnClickedCount > 3){
                    ShowToast.showToast("操作太过频繁，请明天再试！");
                }else {
                    ShowToast.showToast("请填写有效手机号");
                }
               break;

            case R.id.btn_register_next:
                String validateCodeInput = mEtValidateCode.getText().toString(); //获取输入的验证码
                String password = mEtPassword.getText().toString(); //获取输入的密码
                String regex = "[0-9A-Za-z]{6,20}";//6-20位数字或者字母
                if(validateCodeInput.length() == 0 || (!validateCodeInput.equals(validateCodeFromServer))){
                    ShowToast.showToast("验证码不正确，请重新输入验证码！");
                   Log.e("tag",validateCodeInput +":" + validateCodeFromServer);
                }else if(!password.matches(regex)) {
                    ShowToast.showToast("密码只能由6-20位数字或英文组成！");
                }else {
                    Intent intent = new Intent(RegisterActivity.this, PersonalDataActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    //返回验证码
    private String httpPostRequest(final String phoneNumberString) {

        String responseCode = "";

/*        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("phoneNumber",phoneNumberString)
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

                        }else{

                            //ShowToast.showToast("账号或密码有误，请稍后重试");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();*/

        return  "1234";
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
