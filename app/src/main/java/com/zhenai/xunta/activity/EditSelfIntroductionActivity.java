package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ShowToast;

/**
 * 编辑内心独白（自我介绍）页
 * Created by wenjing.tang on 2017/8/2.
 */

public class EditSelfIntroductionActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton mImgBtnBack;
    private TextView mTvSave, mTvWordsCount;
    private EditText mEtSelfIntroduction;
    private int length; //内心独白长度
    private static final int REQUIRED_MINIMUM_LENGTH = 6;//要求的最短长度
    private static final int REQUIRED_MAXIMUM_LENGTH = 20;//要求的最短长度
    private String selfIntroduction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_introduction);

        initViews();

        selfIntroduction = getIntent().getStringExtra("selfIntroduction");
        mEtSelfIntroduction.setText(selfIntroduction);
        length = selfIntroduction.length();
        mTvWordsCount.setText(length + "/" + REQUIRED_MAXIMUM_LENGTH);

        setListeners();

    }

    private void initViews() {
        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        mTvSave = (TextView) findViewById(R.id.tv_save_self_introduction);
        mEtSelfIntroduction = (EditText) findViewById(R.id.et_self_introduction);
        mTvWordsCount = (TextView) findViewById(R.id.tv_words_count);
    }

    private void setListeners() {
        mImgBtnBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mEtSelfIntroduction.addTextChangedListener(new TextChangeListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_back:
                showDialog();
                break;

            case R.id.tv_save_self_introduction:
                if (length < REQUIRED_MINIMUM_LENGTH){
                    ShowToast.showToast("请至少输入" + REQUIRED_MINIMUM_LENGTH +"字~");
                }else if (length > REQUIRED_MAXIMUM_LENGTH){
                    ShowToast.showToast("输入过多哦~");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("selfIntroduction",selfIntroduction); //返回selfIntroduction到上一个活动
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否保存更改");
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
                    ShowToast.showToast("请至少输入" + REQUIRED_MINIMUM_LENGTH +"字~");
                }else if (length > REQUIRED_MAXIMUM_LENGTH){
                    ShowToast.showToast("输入过多哦~");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("selfIntroduction",selfIntroduction);
                    Log.i("selfIntroduction",selfIntroduction);
                    setResult(RESULT_OK, intent);
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
            selfIntroduction = mEtSelfIntroduction.getText().toString();
            length = selfIntroduction.length();
            mTvWordsCount.setText(length + "/" +REQUIRED_MAXIMUM_LENGTH);

            if (length > REQUIRED_MAXIMUM_LENGTH){
                ShowToast.showToast("输入过多哦~");
            }

        }
    }
}
