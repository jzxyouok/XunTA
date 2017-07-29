package com.zhenai.xunta.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.HttpUtil;
import com.zhenai.xunta.utils.ShowToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 注册页面
 * Created by wenjing.tang on 2017/7/26.
 */

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText mEtPhoneNumber, mEtValidateCode, mEtPassword;
    private Button mBtnSendValidateCode, mBtnNext;
    private TextView mTvProtocol, mTvDisclaimer;

    private int btnClickedCount = 0; //记录发送验证码Button点击的次数
    String phoneNumber = "";
    String validateCodeFromServer = "" ; //服务器返回的验证码
    String validateCodeInput = "";
    String password = "";

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
        mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//显示密文

        mBtnSendValidateCode = findViewById(R.id.btn_register_validate_code);
        mBtnNext = findViewById(R.id.btn_register_next);
        mTvProtocol = findViewById(R.id.tv_protocol);
        mTvDisclaimer = findViewById(R.id.tv_disclaimer);

        mTimeCount = new TimeCount(60000, 1000);
    }

    private void setListeners() {
        mBtnSendValidateCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mTvProtocol.setOnClickListener(this);
        mTvDisclaimer.setOnClickListener(this);

        //为EditText绑定监听器
        mEtPhoneNumber.addTextChangedListener(new TextChangeListener(mEtPhoneNumber));
        mEtValidateCode.addTextChangedListener(new TextChangeListener(mEtValidateCode));
        mEtPassword.addTextChangedListener(new TextChangeListener(mEtPassword));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_register_validate_code:
               phoneNumber = mEtPhoneNumber.getText().toString(); //获取输入的手机号
                if (phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$") && (btnClickedCount <=3 )) {

                    HttpUtil.sendPostRequestWithOkHttp("http://10.1.3.39:8080/login","phone", phoneNumber, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ShowToast.showToast("网络异常");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                validateCodeFromServer = jsonObject.getString("resultCode"); //服务器返回码
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    mTimeCount.start();
                    btnClickedCount++;

                }else if(btnClickedCount > 3){
                    ShowToast.showToast("操作太过频繁，请明天再试！");
                }else {
                    ShowToast.showToast("请填写有效手机号");
                }
               break;

            case R.id.tv_protocol:
                toProtocol();
                break;

            case R.id.tv_disclaimer:
                toDisclaimer();
                break;

            case R.id.btn_register_next:
                validateCodeInput = mEtValidateCode.getText().toString(); //获取输入的验证码
                password= mEtPassword.getText().toString(); //获取输入的密码

                if (phoneNumber.length() != 0 && validateCodeFromServer.length() != 0 && password.length() != 0){
                    mBtnNext.isEnabled();
                }

                String regex = "[0-9A-Za-z]{6,20}";//6-20位数字或者字母
                if(validateCodeInput.length() == 0 || (!validateCodeInput.equals(validateCodeFromServer))){
                    ShowToast.showToast("验证码不正确，请重新输入验证码！");
                }else if(!password.matches(regex)) {
                    ShowToast.showToast("密码只能由6-20位数字或英文组成！");
                }else {
                    Intent intent = new Intent(RegisterActivity.this, PersonalDataActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void toProtocol() {
        ScrollView sc = new ScrollView(this);
        sc.setBackgroundColor(getResources().getColor(R.color.white));
        TextView tv = new TextView(this);
        tv.setTextSize(16);
        tv.setText(R.string.protocol);
        sc.addView(tv);

        new AlertDialog.Builder(this)
                .setTitle("用户协议")
                .setView(sc)
                .create()
                .show();
    }

    private void toDisclaimer() {
        ScrollView sc = new ScrollView(this);
        sc.setBackgroundColor(getResources().getColor(R.color.white));
        TextView tv = new TextView(this);
        tv.setTextSize(16);
        tv.setText(R.string.disclaimer);
        sc.addView(tv);

       AlertDialog dialog =  new AlertDialog.Builder(this)
                .setTitle("免责声明")
                .setView(sc)
                .create();

        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
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

    class TextChangeListener implements TextWatcher{

        private EditText editText;

        public TextChangeListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            phoneNumber =  mEtPhoneNumber.getText().toString(); //获取输入的手机号
            validateCodeInput = mEtValidateCode.getText().toString();
            password = mEtPassword.getText().toString();

            if (phoneNumber.length()>0 && validateCodeInput.length()>0 && password.length()>0){
                mBtnNext.setEnabled(true);
            }else{
                mBtnNext.setEnabled(false);
            }
        }

    }
}
