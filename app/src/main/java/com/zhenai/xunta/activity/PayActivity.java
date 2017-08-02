package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ShowToast;

/**
 * 支付页面
 * Created by wenjing.tang on 2017/8/2.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton mImageBtnZhifubaoPay,mImageBtnWechatPay, mImageBtnZhifubaoFastPay, mImageBtnUnionPay;
    private Button mBtnQueryPay;
    private int moneyShouldPay; //从上一个活动获取支付金额
    private boolean isSelected = false; //是否选择了支付工具
    String title;//dialog标题
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initviews();

        moneyShouldPay = getIntent().getIntExtra("money",0);

        setListeners();

    }

    private void initviews() {
        mImageBtnZhifubaoPay = (ImageButton) findViewById(R.id.imgBtn_zhifubao_pay);
        mImageBtnWechatPay = (ImageButton) findViewById(R.id.imgBtn_wechat_pay);
        mImageBtnZhifubaoFastPay = (ImageButton) findViewById(R.id.imgBtn_zhifubao_fast_pay);
        mImageBtnUnionPay = (ImageButton) findViewById(R.id.imgBtn_union_pay);
        mBtnQueryPay = (Button) findViewById(R.id.btn_query_pay);
    }
    private void setListeners() {
        mImageBtnZhifubaoPay.setOnClickListener(this);
        mImageBtnWechatPay.setOnClickListener(this);
        mImageBtnZhifubaoFastPay.setOnClickListener(this);
        mImageBtnUnionPay.setOnClickListener(this);
        mBtnQueryPay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_zhifubao_pay:
                mImageBtnZhifubaoPay.setBackgroundResource(R.drawable.pay_selected);
                mImageBtnWechatPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnZhifubaoFastPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnUnionPay.setBackgroundResource(R.drawable.pay_normal);
                isSelected = true;
                title = "支付宝支付";
                break;

            case R.id.imgBtn_wechat_pay:
                mImageBtnZhifubaoPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnWechatPay.setBackgroundResource(R.drawable.pay_selected);
                mImageBtnZhifubaoFastPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnUnionPay.setBackgroundResource(R.drawable.pay_normal);
                title = "微信支付";
                isSelected = true;
                break;

            case R.id.imgBtn_zhifubao_fast_pay:
                mImageBtnZhifubaoPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnWechatPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnZhifubaoFastPay.setBackgroundResource(R.drawable.pay_selected);
                mImageBtnUnionPay.setBackgroundResource(R.drawable.pay_normal);
                title = "支付宝快捷支付";
                isSelected = true;
                break;

            case R.id.imgBtn_union_pay:
                mImageBtnZhifubaoPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnWechatPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnZhifubaoFastPay.setBackgroundResource(R.drawable.pay_normal);
                mImageBtnUnionPay.setBackgroundResource(R.drawable.pay_selected);
                title = "银联支付";
                isSelected = true;
                break;
            case R.id.btn_query_pay:
                //isSelected = true 与 isSelected = true 的区别是后者是赋值语句，总是执行，不会判断
                //所以最好写成true == isSelected，养成好习惯，已知量写在前面
                if (true == isSelected){
                    showDialog();
                }else{
                    ShowToast.showToast("请选择支付工具");
                }
                break;
        }

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage("您需要支付"+ moneyShouldPay +" 元");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 2017/8/2 支付操作 
                ShowToast.showToast("支付成功");
            }
        });
        builder.show();
    }
}
