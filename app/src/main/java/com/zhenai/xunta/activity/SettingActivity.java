package com.zhenai.xunta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ActivityCollector;
import com.zhenai.xunta.utils.CleanMessageUtil;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.widget.ItemSettingLayout;

/**
 * Created by wenjing.tang on 2017/7/29.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Button mBtnLogout;
    private ItemSettingLayout mChangePassword, mFeedback, mReport, mAboutUs, mWipeCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();

        setListeners();
    }

    private void setListeners() {
        mBtnLogout.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mReport.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mWipeCache.setOnClickListener(this);
    }

    private void initViews() {
       // View rootView = LayoutInflater.from(this).inflate(R.layout.activity_setting, null);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
        mChangePassword = (ItemSettingLayout) findViewById(R.id.change_password);
        mFeedback =  (ItemSettingLayout) findViewById(R.id.feedback);
        mReport =  (ItemSettingLayout) findViewById(R.id.report);
        mAboutUs =  (ItemSettingLayout) findViewById(R.id.about_us);
        mWipeCache =  (ItemSettingLayout) findViewById(R.id.wipe_cache);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_password:
                startActivity(new Intent(SettingActivity.this, ForgetPasswordActivity.class));
                break;

            case R.id.feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;

            case R.id.report:
                break;

            case R.id.about_us:
                break;

            case R.id.wipe_cache:
                try {
                    String cacheSize =CleanMessageUtil.getTotalCacheSize(this);
                    CleanMessageUtil.clearAllCache(this);
                    ShowToast.showToast("清除缓存"+ cacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_logout:
                SharedPreferencesUtil.removeParam(SettingActivity.this,"phone"); //清除保存的手机号

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
