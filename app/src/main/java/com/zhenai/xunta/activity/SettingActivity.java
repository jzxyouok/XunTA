package com.zhenai.xunta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ActivityCollector;
import com.zhenai.xunta.utils.SharedPreferencesUtil;

/**
 * Created by wenjing.tang on 2017/7/29.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Button mBtnLogout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();

        setListeners();
    }

    private void setListeners() {
        mBtnLogout.setOnClickListener(this);
    }

    private void initViews() {
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logout:
                SharedPreferencesUtil.removeParam(SettingActivity.this,"phone"); //清除手机号

                //startActivity(new Intent(SettingActivity.this, LaunchActivity.class));
                ActivityCollector.finishAll();

                //启动登陆活动
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }
    }
}
