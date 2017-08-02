package com.zhenai.xunta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.zhenai.xunta.R;

/**
 * 会员中心
 * Created by wenjing.tang on 2017/8/1.
 */

public class MemberCenterActivity extends  BaseActivity {

    private RadioButton mRadioBtnSixMonth, mRadioBtnThreeMonth, mRadioBtnOneMonth;
    private Button mBtnQuery;
    int moneyOfMember = 299;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_membership);

        intViews();

        mRadioBtnSixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnSixMonth.isChecked()){
                    mRadioBtnThreeMonth.setChecked(false);
                    mRadioBtnOneMonth.setChecked(false);
                }
                moneyOfMember = 299;
            }
        });

        mRadioBtnThreeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnThreeMonth.isChecked()){
                    mRadioBtnSixMonth.setChecked(false);
                    mRadioBtnOneMonth.setChecked(false);
                }
                moneyOfMember = 239;
            }
        });

        mRadioBtnOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadioBtnOneMonth.isChecked()){
                    mRadioBtnSixMonth.setChecked(false);
                    mRadioBtnThreeMonth.setChecked(false);
                }
                moneyOfMember = 99;
            }
        });


        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberCenterActivity.this, PayActivity.class);
                intent.putExtra("money", moneyOfMember);
                startActivity(intent);
            }
        });
    }

    private void intViews() {
        mRadioBtnSixMonth = (RadioButton) findViewById(R.id.rb_six_month);
        mRadioBtnThreeMonth = (RadioButton) findViewById(R.id.rb_three_month);
        mRadioBtnOneMonth = (RadioButton) findViewById(R.id.rb_one_month);
        mBtnQuery = (Button) findViewById(R.id.btn_query_purchase);
    }
}
