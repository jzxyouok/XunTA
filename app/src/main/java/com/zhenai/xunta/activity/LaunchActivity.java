package com.zhenai.xunta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zhenai.xunta.R;

/**
 *启动APP的主界面
 * Created by wenjing.tang on 2017/7/26.
 */

public class LaunchActivity extends Activity implements View.OnClickListener {

    private Button mRegisterBtn, mLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mRegisterBtn = findViewById(R.id.btn_launch_activity_login);
        mLoginBtn = findViewById(R.id.btn_launch_activity_register);

        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_launch_activity_login:
                Intent gotoLoginActivityIntent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(gotoLoginActivityIntent);
                break;

            case R.id.btn_launch_activity_register:
                Intent gotoLoginRegisterIntent = new Intent(LaunchActivity.this, RegisterActivity.class);
                startActivity(gotoLoginRegisterIntent);
                break;
        }

    }
}
