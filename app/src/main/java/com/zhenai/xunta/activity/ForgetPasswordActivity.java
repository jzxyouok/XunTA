package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtPhoneNumber, mEtValidateCode, mEtPassword;
    private Button mBtnSendValidateCode, mBtnNext;

    private int btnClickedCount = 0; //记录发送验证码Button点击的次数
    String phoneNumber;
    String validateCodeFromServer ; //服务器返回的验证码
    String stateCode; ////服务器返回的状态码

    private TimeCount mTimeCount;//计时器

    public static final String FORGET_PASSWORD_SUCCESS = "1701011";
    public static final String FORGET_PASSWORD_FAILURE = "1701012";
    public static  final String PHONE_IS_NOT_EXISIT = "1701013";
    public static  final String FORGET_PASSWORD_URL = "http://10.1.3.39:8080/login";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);

        initViews();

        setListeners();
    }

    private void initViews() {
        mEtPhoneNumber = (EditText) findViewById(R.id.et_forgetpassword_telphone_number);
        mEtValidateCode = (EditText) findViewById(R.id.et_forgetpassword_validate_code);
        mEtPassword = (EditText) findViewById(R.id.et_forgetpassword_password);

        mBtnSendValidateCode = (Button) findViewById(R.id.btn_forgetpassword__validate_code);
        mBtnNext = (Button) findViewById(R.id.btn_forget_password_query);

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

                    HttpUtil.sendPostRequestWithOkHttp(FORGET_PASSWORD_URL,"phone", phoneNumber, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ShowToast.showToast("网络异常，请稍后再试~");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                           String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                // TODO: 2017/8/1  获取返回的 状态码 和 验证码
                                stateCode = "";
                                 validateCodeFromServer = jsonObject.getString("data"); //获取服务器返回的验证码

                                if(stateCode.equals(PHONE_IS_NOT_EXISIT)){//账号不存在，去注册
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog();
                                        }
                                    });
                                }
                                if(validateCodeFromServer.equals(FORGET_PASSWORD_SUCCESS)){ //

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    mTimeCount.start();
                    btnClickedCount++;

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

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您的账号不存在");
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
                Intent intent = new Intent(ForgetPasswordActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        builder.show();

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
