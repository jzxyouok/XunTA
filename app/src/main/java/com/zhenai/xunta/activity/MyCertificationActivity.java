package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.HttpUtil;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.widget.LoadingCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我的认证页面
 * Created by wenjing.tang on 2017/7/29.
 */

public class MyCertificationActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTvYanzhiCertification,  mTvFuhaoCertification, mTvIDCertification;
    private ImageButton mImgBtnYanzhiCertification, mImgBtnFuhaoCertification, mImgBtnIDCertification;

    private String yanzhiCertificationStatus, fuhaoCertificationStatus, IDCertificationStatus;

    public static final int TAKE_PHOTO = 1;
    File outputImageFile;
    private Uri imageUri;
    int faceScore;

    //认证三种状态
    private static final String HAS_CERTIFICATED = "已认证";
    private static final String CLICK_TO_CERTIFICATE = "点击认证";
    private static final String FAIL_TO_PASS_CERTIFICATION = "认证未通过";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_certification);

        initViews();
        initDatas();
    }

    private void initViews() {
        mTvYanzhiCertification = (TextView) findViewById(R.id.tv_yanzhi_certification);
        mTvFuhaoCertification = (TextView) findViewById(R.id.tv_fuhao_certification);
        mTvIDCertification = (TextView) findViewById(R.id.tv_id_certification);
        mImgBtnYanzhiCertification = (ImageButton) findViewById(R.id.imgBtn_yanzhi_certification);
        mImgBtnFuhaoCertification = (ImageButton) findViewById(R.id.imgBtn_fuhao_certification);
        mImgBtnIDCertification = (ImageButton) findViewById(R.id.imgBtn_id_certification);

        mImgBtnYanzhiCertification.setOnClickListener(this);
        mImgBtnFuhaoCertification.setOnClickListener(this);
        mImgBtnIDCertification.setOnClickListener(this);

        yanzhiCertificationStatus = mTvYanzhiCertification.getText().toString();
        fuhaoCertificationStatus= mTvFuhaoCertification.getText().toString();
        IDCertificationStatus = mTvIDCertification.getText().toString();
    }
    private void initDatas() {
        SharedPreferencesUtil.getParam(this,"yanzhiCertificationStatus",yanzhiCertificationStatus);
        // TODO: 2017/8/4  初始化数据，向服务器请求，再设置三种认证状态
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_yanzhi_certification:
                if (yanzhiCertificationStatus.equals(HAS_CERTIFICATED)){
                    ShowToast.showToast("您已认证了哦~");
                }else {
                    takePhoto();
                }
                break;

            case R.id.imgBtn_fuhao_certification:
                if (fuhaoCertificationStatus.equals(HAS_CERTIFICATED)){
                    ShowToast.showToast("您已认证了哦~");
                }else {
                    startActivity(new Intent(MyCertificationActivity.this, FuhaoCertificationActivity.class));
                }
                break;

            case R.id.imgBtn_id_certification:
                if (IDCertificationStatus.equals(HAS_CERTIFICATED)){
                    ShowToast.showToast("您已认证了哦~");
                }else {
                startActivity(new Intent(MyCertificationActivity.this, IDCertificationActivity.class));
                }
                break;
        }

    }

    public void takePhoto(){
        // 创建File对象，用于存储拍照后的图片
        outputImageFile = new File(getExternalCacheDir(), "yanzhi_certification.jpg");
        try {
            if (outputImageFile.exists()) {
                outputImageFile.delete();
            }
            outputImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImageFile);
        } else {
            imageUri = FileProvider.getUriForFile(MyCertificationActivity.this, "com.example.cameraalbumtest.fileprovider", outputImageFile);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                LoadingCustomDialog.showProgress(MyCertificationActivity.this, "正在认证颜值~", true);//显示dialog

                                // TODO: 2017/8/4 没对接口 拿到分数
                                HttpUtil.sendFileWithOkHttp("xxx", "facePhoto",outputImageFile, new Callback() { //将照片上传到服务器
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        ShowToast.showToast("上传失败，请稍后再试~");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        LoadingCustomDialog.dismissProgress();

                                        String responseData = response.body().string();

                                        try {
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            faceScore = jsonObject.getInt("score"); //获取分数
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        }).start();


                        // 从服务器拿到分数 ，如果分数>80，设置textview为已认证，否则设为认证未通过
                       // faceScore = 70; //模拟后台数据
                        if (faceScore >= 80){
                            yanzhiCertificationStatus = HAS_CERTIFICATED;
                            mTvYanzhiCertification.setText(HAS_CERTIFICATED);
                            SharedPreferencesUtil.setParam(this,"yanzhiCertificationStatus", yanzhiCertificationStatus);
                            showFaceScoreHighDialog();
                        }else {
                            yanzhiCertificationStatus = FAIL_TO_PASS_CERTIFICATION;
                            mTvYanzhiCertification.setText(FAIL_TO_PASS_CERTIFICATION);
                            showFaceScoreLowDialog();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    private void showFaceScoreHighDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("颜值"+faceScore+ "分！" +"\n哎呀，你颜值真高！\n继续进行车房认证可以和更多人相约哟！");
        builder.setNegativeButton("直接进入APP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(MyCertificationActivity.this, MainActivity.class));
            }
        });
        builder.setPositiveButton("车房认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(MyCertificationActivity.this, FuhaoCertificationActivity.class));

            }
        });
        builder.show();
    }

    private void showFaceScoreLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("颜值"+faceScore+ "分！" + "哎呀，你颜值不够哦！\n进行车房认证也可以进入APP可以和更多人相约哟！\n或者换个角度,重新拍摄吧");
        builder.setNegativeButton("车房认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(MyCertificationActivity.this, FuhaoCertificationActivity.class));
            }
        });
        builder.setPositiveButton("换个角度拍摄", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                takePhoto();
            }
        });
        builder.show();
    }
}
