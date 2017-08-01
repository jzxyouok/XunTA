package com.zhenai.xunta.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ActivityCollector;

/**
 *启动APP的主界面
 * Created by wenjing.tang on 2017/7/26.
 */

public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    private Button mRegisterBtn, mLoginBtn;

    private FinishLaunchActivityBroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mRegisterBtn = (Button) findViewById(R.id.btn_launch_activity_login);
        mLoginBtn = (Button) findViewById(R.id.btn_launch_activity_register);

        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        receiver = new FinishLaunchActivityBroadcastReceiver();
        registerReceiver(receiver,new IntentFilter("com.xunta.FINISH_LAUNCH_ACTIVITY__BROADCAST"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

    //动态广播
    public class FinishLaunchActivityBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.xunta.FINISH_LAUNCH_ACTIVITY__BROADCAST")){
                ActivityCollector.finishActivity(LaunchActivity.this); //finish();
            }

        }
    }
}
