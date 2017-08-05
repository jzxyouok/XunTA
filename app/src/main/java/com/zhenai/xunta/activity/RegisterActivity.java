package com.zhenai.xunta.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ActivityCollector;
import com.zhenai.xunta.utils.HttpUtil;
import com.zhenai.xunta.utils.ServerUrl;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtPhoneNumber, mEtValidateCode, mEtPassword;
    private Button mBtnSendValidateCode, mBtnNext;
    private TextView mTvProtocol, mTvDisclaimer;

    String validateCodeInput = "";
    String phoneNumber = "";
    String password = "";

    private int btnClickedCount = 0; //记录发送验证码Button点击的次数
    private TimeCount mTimeCount;//计时器

    public static final String SEND_MESSAGE_SUCCESS = "1701001";
    public static final String SEND_MESSAGE_FAILURE= "1701002";
    public static final String PHONE_EXISTED= "1701003";

    public static final String CHECK_MESSAGE_SUCCESS = "1701004";
    public static final String CHECK_MESSAGE_FAILURE = "1701005";

    String sendMessageResultCode ;//发送消息返回码
    String checkResultCode;//验证消息返回码

    private FinishRegisterActivityBroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initViews();

        setListeners();

        receiver = new FinishRegisterActivityBroadcastReceiver();
        registerReceiver(receiver,new IntentFilter("com.xunta.FINISH_REGISTER_ACTIVITY__BROADCAST"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //动态广播
    public class FinishRegisterActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.xunta.FINISH_REGISTER_ACTIVITY__BROADCAST")){
                ActivityCollector.finishActivity(RegisterActivity.this); //finish();
            }

        }
    }

    private void initViews() {
        mEtPhoneNumber = (EditText) findViewById(R.id.et_register_telphone_number);
        mEtValidateCode = (EditText) findViewById(R.id.et_register_validate_code);

        mEtPassword = (EditText) findViewById(R.id.et_register_password);
        mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());//显示密文

        mBtnSendValidateCode = (Button) findViewById(R.id.btn_register_validate_code);
        mBtnNext = (Button) findViewById(R.id.btn_register_next);
        mTvProtocol = (TextView) findViewById(R.id.tv_protocol);
        mTvDisclaimer = (TextView) findViewById(R.id.tv_disclaimer);

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

            case R.id.btn_register_validate_code://发送验证码， get请求
               phoneNumber = mEtPhoneNumber.getText().toString();
                if (phoneNumber.length() == 11 && phoneNumber.matches("^1[34578]\\d{9}$") && (btnClickedCount <=3 )) {

                    String url = ServerUrl.SEND_MESSAGE_URL + "?" + "phone=" + phoneNumber;

                    HttpUtil.sendGetRequestWithOkHttp(url, new okhttp3.Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowToast.showToast("网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                sendMessageResultCode = jsonObject.getString("resultCode");
                                Log.e("resultCode",sendMessageResultCode);

                                if (sendMessageResultCode.equals(SEND_MESSAGE_SUCCESS)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowToast.showToast("发送成功");
                                        }
                                    });

                                }else if(sendMessageResultCode.equals(SEND_MESSAGE_FAILURE)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowToast.showToast("发送失败");
                                        }
                                    });

                                }else if(sendMessageResultCode.equals(PHONE_EXISTED)){ //
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showPhoneExistedDialog();
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );

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

                if (phoneNumber.length() != 0 && validateCodeInput.length() != 0 && password.length() != 0){
                    mBtnNext.isEnabled();
                }

                String regex = "[0-9A-Za-z]{6,20}";//6-20位数字或者字母
                if(!password.matches(regex)) {
                    ShowToast.showToast("密码只能由6-20位数字或英文组成！");
                }else {
                    String url = ServerUrl.CHECK_MESSAGE_URL + "?" + "phone=" + phoneNumber +"&" + "messageCode=" + validateCodeInput;
                    HttpUtil.sendGetRequestWithOkHttp(url, new okhttp3.Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowToast.showToast("网络异常");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                checkResultCode = jsonObject.getString("resultCode");
                                Log.e("resultCode",checkResultCode);

                               if (checkResultCode.equals(CHECK_MESSAGE_SUCCESS)){
                                   Intent intent = new Intent(RegisterActivity.this, PersonalDataActivity.class);
                                   intent.putExtra("phone",phoneNumber);
                                   intent.putExtra("password",password);
                                   startActivity(intent);

                               }else {
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          ShowToast.showToast("验证码输入错误");
                                      }
                                  });

                               }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );

                }

                break;
        }
    }

    private void showPhoneExistedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您已注册手机号");
        builder.setMessage("点击确定进行登录");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
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

    /*
    监听EditText，当EditText全部不为空时Button才可以点击，否则不能点击
     */
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
