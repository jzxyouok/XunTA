package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ActivityCollector;
import com.zhenai.xunta.utils.CleanMessageUtil;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.widget.ItemSettingLayout;

/**
 * 设置页
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

       // initDatas();
    }

    private void initViews() {
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
        mChangePassword = (ItemSettingLayout) findViewById(R.id.change_password);
        mFeedback =  (ItemSettingLayout) findViewById(R.id.feedback);
        mReport =  (ItemSettingLayout) findViewById(R.id.report);
        mAboutUs =  (ItemSettingLayout) findViewById(R.id.about_us);
        mWipeCache =  (ItemSettingLayout) findViewById(R.id.wipe_cache);
    }

    private void setListeners() {
        mBtnLogout.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mReport.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mWipeCache.setOnClickListener(this);
    }

    private void initDatas() {
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
                    String cacheSize = CleanMessageUtil.getTotalCacheSize(this);

                    if (cacheSize.equals("0KB")){
                        ShowToast.showToast("本地没有缓存哦~");
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("清除缓存");
                        builder.setMessage("确定要清除 "+ cacheSize + " 缓存吗？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CleanMessageUtil.clearAllCache(SettingActivity.this);
                                ShowToast.showToast("清除成功");
                            }
                        });
                        builder.show();
                    }

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
