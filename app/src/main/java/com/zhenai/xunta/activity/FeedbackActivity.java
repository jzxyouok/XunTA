package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ShowToast;

/**
 * 用户反馈 页面
 * Created by wenjing.tang on 2017/8/1.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton mImgBtnBack;
    private EditText mEtFeedback;
    private Button mBtnFeedback;
    private TextView mTvWordsCount;
    String feedback;
    int length;
    private static final int REQUIRED_MINIMUM_LENGTH = 6;//要求的最短长度
    private static final int REQUIRED_MAXIMUM_LENGTH = 1500;//要求的最长长度
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        initViews();

        setListeners();

        feedback = mEtFeedback.getText().toString();
        length = feedback.length();
        mTvWordsCount.setText(length + "/" + REQUIRED_MAXIMUM_LENGTH );
    }

    private void initViews() {
        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        mBtnFeedback = (Button) findViewById(R.id.btn_submit_feedback);
        mEtFeedback = (EditText) findViewById(R.id.et_feedback);
        mTvWordsCount = (TextView) findViewById(R.id.tv_words_count);
    }

    private void setListeners() {
        mImgBtnBack.setOnClickListener(this);
        mBtnFeedback.setOnClickListener(this);
        mEtFeedback.addTextChangedListener(new TextChangeListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_back:
                if (0 == length ){
                    finish();
                }else {
                    showDialog();
                }
                break;

            case R.id.btn_submit_feedback:
                if (length < 6 ){
                    ShowToast.showToast("请至少输入" + REQUIRED_MINIMUM_LENGTH +"个字~");
                }else {

                    // TODO: 2017/8/7 提交反馈给服务器
                    ShowToast.showToast("反馈已提交，真诚感谢您的宝贵意见~");
                    finish();
                }


                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否提交反馈");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (length < REQUIRED_MINIMUM_LENGTH){
                    ShowToast.showToast("请至少输入" + REQUIRED_MINIMUM_LENGTH +"个字~");
                }else if (length > REQUIRED_MAXIMUM_LENGTH){
                    ShowToast.showToast("输入过多哦~");
                }else {

                    // TODO: 2017/8/7 提交反馈给服务器

                    ShowToast.showToast("反馈已提交，真诚感谢您的宝贵意见~");
                    finish();
                }
            }
        });
        builder.show();
    }

    /*
      监听EditText，实时改变输入字数
    */
    class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            feedback = mEtFeedback.getText().toString();
            length = feedback.length();
            mTvWordsCount.setText(length + "/" +REQUIRED_MAXIMUM_LENGTH);

            if (length > REQUIRED_MAXIMUM_LENGTH){
                ShowToast.showToast("输入过多哦~");
            }

        }
    }
}
