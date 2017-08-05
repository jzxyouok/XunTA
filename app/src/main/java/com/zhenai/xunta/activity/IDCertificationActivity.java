package com.zhenai.xunta.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liji.takephoto.TakePhoto;
import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.ShowToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 身份证认证
 * Created by wenjing.tang on 2017/8/4.
 */

public class IDCertificationActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mIvUploadIDCard1, mIvUploadIDCard2;
    private Button mBtnSubmit;

    List<File> imageFileList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_id_certification);

        initViews();

        setListeners();
    }

    private void initViews() {
        mIvUploadIDCard1 = (ImageView) findViewById(R.id.iv_upload_id_card_1);
        mIvUploadIDCard2 = (ImageView) findViewById(R.id.iv_upload_id_card_2);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    private void setListeners() {
        mIvUploadIDCard1.setOnClickListener(this);
        mIvUploadIDCard2.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_upload_id_card_1:
                TakePhoto takePhoto = new TakePhoto(IDCertificationActivity.this);
                takePhoto.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {
                        imageFileList.add(new File(path));
                        Glide.with(IDCertificationActivity.this).load("file://" + path).into(mIvUploadIDCard1);
                    }
                });
                takePhoto.show();
                break;

            case R.id.iv_upload_id_card_2:
                TakePhoto takePhoto2 = new TakePhoto(IDCertificationActivity.this);
                takePhoto2.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {

                        Log.e("path", path);
                        File file = new File(path);
                        imageFileList.add(file);
                        Glide.with(IDCertificationActivity.this).load(file).into(mIvUploadIDCard2);
                    }
                });
                takePhoto2.show();
                break;

            case R.id.btn_submit:

                if (imageFileList.size() ==0 ){
                    ShowToast.showToast("请上传图片后再认证");
                }else {

                    // TODO: 2017/8/4 向服务器提交数据
                }

                break;

        }
    }
}
