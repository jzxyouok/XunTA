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
 * 富豪认证页面
 * Created by wenjing.tang on 2017/8/4.
 */

public class FuhaoCertificationActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mIvUploadHouseLicense, mIvUploadCarPicture;
    private Button mBtnCertificate;

    List<File> imageFileList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_and_car_certification);

        initViews();

        setListeners();

    }

    private void initViews() {
        mIvUploadHouseLicense = (ImageView) findViewById(R.id.iv_upload_house_license);
        mIvUploadCarPicture = (ImageView) findViewById(R.id.iv_upload_car_picture);
        mBtnCertificate = (Button) findViewById(R.id.btn_certificate);
    }

    private void setListeners() {
        mIvUploadHouseLicense.setOnClickListener(this);
        mIvUploadCarPicture.setOnClickListener(this);
        mBtnCertificate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_upload_house_license:
                TakePhoto takePhoto = new TakePhoto(FuhaoCertificationActivity.this);
                takePhoto.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {
                        imageFileList.add(new File(path));
                        Glide.with(FuhaoCertificationActivity.this).load("file://" + path).into(mIvUploadHouseLicense);
                    }
                });
                takePhoto.show();
                break;

            case R.id.iv_upload_car_picture:
                TakePhoto takePhoto2 = new TakePhoto(FuhaoCertificationActivity.this);
                takePhoto2.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {

                        Log.e("path", path);
                        File file = new File(path);
                        imageFileList.add(file);
                        Glide.with(FuhaoCertificationActivity.this).load(file).into(mIvUploadCarPicture);
                        // Glide.with(FuhaoCertificationActivity.this).load("file://" + path).into(mIvUploadCarPicture);
                    }
                });
                takePhoto2.show();
                break;

            case R.id.btn_certificate:

                //Log.e("path",imageFileList.size() + "");
                if (imageFileList.size() ==0 ){
                    ShowToast.showToast("请上传图片后再认证");
                }else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2017/8/4 向服务器上传图片 ，这里有1张 或 2张 的情况
                        }
                    }).start();
                }

                break;

        }
    }

}
