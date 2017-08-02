package com.zhenai.xunta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.zhenai.xunta.R;

/**
 * 我的聊天券
 * Created by wenjing.tang on 2017/8/1.
 */

public class MyChatCouponActivity extends  BaseActivity {

    private RadioButton mRadioBtnTenPics, mRadioBtnFivePics, mRadioBtnOnePics;
    private Button mBtnQuery;
    int moneyOfCoupon = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_appointment_coupon);

        intViews();

        mRadioBtnTenPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnTenPics.isChecked()){
                    mRadioBtnFivePics.setChecked(false);
                    mRadioBtnOnePics.setChecked(false);
                    moneyOfCoupon = 30;
                }
            }
        });

        mRadioBtnFivePics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnFivePics.isChecked()){
                    mRadioBtnTenPics.setChecked(false);
                    mRadioBtnOnePics.setChecked(false);
                    moneyOfCoupon = 20;
                }
            }
        });

        mRadioBtnOnePics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnOnePics.isChecked()){
                    mRadioBtnTenPics.setChecked(false);
                    mRadioBtnFivePics.setChecked(false);
                    moneyOfCoupon = 6;
                }
            }
        });


        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChatCouponActivity.this, PayActivity.class);
                intent.putExtra("money",moneyOfCoupon);
                startActivity(intent);
            }
        });
    }

    private void intViews() {
        mRadioBtnTenPics = (RadioButton) findViewById(R.id.rb_ten_pics);
        mRadioBtnFivePics = (RadioButton) findViewById(R.id.rb_five_pics);
        mRadioBtnOnePics = (RadioButton) findViewById(R.id.rb_one_pics);
        mBtnQuery = (Button) findViewById(R.id.btn_query_purchase);
    }
}
