package com.zhenai.xunta.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.zhenai.xunta.R;
import com.zhenai.xunta.model.JsonBean;
import com.zhenai.xunta.utils.GetJsonDataUtil;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.widget.CustomPopupWindow;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人资料
 * Created by wenjing.tang on 2017/7/27.
 */

public class PersonalDataActivity extends Activity implements View.OnClickListener, CustomPopupWindow.OnItemClickListener {

    private LinearLayout mLinearLayoutAvatar;
    private TextView mTvHint;
    private EditText mEtNickname;
    private RadioButton mRadioBtnMale,mRadioBtnFemale;
    private Button mBtnDistrict, mBtnBirthDate, mBtnDone;

    private CustomPopupWindow mPop;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;

    //城市数据源
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private String nickName, sex, districtName, birthdata ;

    boolean isImageUploaded = false;
    boolean isTimeSelected = false;
    boolean isDistrictSelected = false;

    File outputImage = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        initViews();
        setListeners();

        initJsonData();
        mPop=new CustomPopupWindow(this);
        mPop.setOnItemClickListener(this);
    }

    public void initViews() {
        mLinearLayoutAvatar = findViewById(R.id.ll_upload_avatar);
        mTvHint = findViewById(R.id.tv_hint);
        mEtNickname = findViewById(R.id.et_set_nickname);
        mRadioBtnMale = findViewById(R.id.rb_male);
        mRadioBtnFemale = findViewById(R.id.rb_female);

        mBtnDistrict = findViewById(R.id.btn_select_district);
        mBtnDistrict.getBackground().setAlpha(0);

        mBtnBirthDate = findViewById(R.id.btn_select_birthdate);
        mBtnBirthDate.getBackground().setAlpha(0);
        mBtnDone = findViewById(R.id.btn_done);
    }

    public void setListeners() {
        mLinearLayoutAvatar.setOnClickListener(this);
        mBtnDistrict.setOnClickListener(this);
        mBtnBirthDate.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_upload_avatar:
                mPop.showAtLocation(PersonalDataActivity.this.findViewById(R.id.btn_done), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.btn_select_district:
                ShowPickerView();
                break;

            case R.id.btn_select_birthdate:
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();

                //正确设置方式 原因：注意事项有说明
                selectedDate.set(2000,0,1);
                startDate.set(1950,0,1);
                endDate.set(2030,11,31);
                //时间选择器
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        birthdata = sdf.format(date);
                        mBtnBirthDate.setText(birthdata);
                        Log.e("tag",birthdata);
                        isTimeSelected = true;
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setTitleText("出生日期")//标题文字
                        .isCyclic(true)//是否循环滚动
                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                        .setRangDate(startDate,endDate)//起始终止年月日设定
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .build();
                pvTime.show();
                break;

            case R.id.btn_done: //完成，提交数据到服务器并进入主界面
                String regex = "[a-zA-Z0-9_\u4e00-\u9fa5]+";
                String regexPureNumber = "^\\d+$";
                nickName = mEtNickname.getText().toString();
                if(isImageUploaded == false) {
                    ShowToast.showToast("未上传头像哦~");
                }else if (nickName.length() == 0){
                    ShowToast.showToast("请输入昵称~");
                }else if(nickName.length() > 8){
                    ShowToast.showToast("昵称太长啦~");
                }else if (nickName.matches(regexPureNumber)){
                    ShowToast.showToast("昵称不能由纯数字组成组成~");
                }else if (!nickName.matches(regex)){
                    ShowToast.showToast("昵称只能由汉字、数字、字母和下划线组成~");
                }else if(nickName.length() > 8){
                    ShowToast.showToast("昵称太长啦~");
                }else if(!mRadioBtnMale.isChecked() && !mRadioBtnFemale.isChecked()){
                    ShowToast.showToast("请选择性别");
                }else if(isDistrictSelected == false){
                    ShowToast.showToast("请选择城市");
                }else if(isTimeSelected == false){
                    ShowToast.showToast("请选择出生日期");
                }else{
                    // TODO: 2017/7/27   向服务器发请求
                    postPersonalData();

                    //跳转到登录页
                    Intent intent = new Intent(PersonalDataActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    public void postPersonalData() {

        Map<String, String> map = new HashMap<>();
        nickName = mEtNickname.getText().toString();
        Log.e("tag",nickName);
        if (mRadioBtnMale.isChecked()){
             sex = "男";
        }else {
            sex = "女";
        }
        Log.e("tag",sex);
        if (outputImage.exists()){

        }else {
            Log.e("tag","头像不存在");
        }

    }

    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                take_photo();
                mPop.dismiss();
                break;
            case R.id.btn_select_from_album:
                if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
                mPop.dismiss();
                break;
            case R.id.btn_cancel:
                mPop.dismiss();
                break;
        }

    }

    public void take_photo(){
        // 创建File对象，用于存储拍照后的图片
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(PersonalDataActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您取消了权限~~", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // Glide将照片加载到LinearLayout背景，将拍摄的照片显示出来
                        Glide.with(this)
                                .load(imageUri)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(180, 180) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        Drawable drawable = new BitmapDrawable(resource);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            mLinearLayoutAvatar.setBackground(drawable);
                                            isImageUploaded = true;
                                        }
                                    }
                                });
                       // Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //mLinearLayoutAvatar.setBackground(new BitmapDrawable(bitmap));
                        mTvHint.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    public void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    public void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    public String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mLinearLayoutAvatar.setBackground(new BitmapDrawable(bitmap));
            isImageUploaded = true;
            mTvHint.setVisibility(View.INVISIBLE);
        } else {
            ShowToast.showToast("获取头像失败");
        }
    }

    public void initJsonData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                /**
                 * assets 目录下的Json文件，实际使用可自行替换文件
                 * 关键逻辑在于循环体
                 * */
                String JsonData = new GetJsonDataUtil().getJson(PersonalDataActivity.this,"province.json");//获取assets目录下的json文件数据
                ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

                /**
                 * 添加省份数据
                 *
                 * 如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
                 * PickerView会通过getPickerViewText方法获取字符串显示出来。
                 */
                options1Items = jsonBean;

                for (int i=0;i<jsonBean.size();i++){//遍历省份
                    ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                    ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                    for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                        String CityName = jsonBean.get(i).getCityList().get(c).getName();
                        CityList.add(CityName);//添加城市

                        ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        if (jsonBean.get(i).getCityList().get(c).getArea() == null
                                ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                            City_AreaList.add("");
                        }else {

                            for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                                String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                                City_AreaList.add(AreaName);//添加该城市所有地区数据
                            }
                        }
                        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                    }

                    /**
                     * 添加城市数据
                     */
                    options2Items.add(CityList);

                    /**
                     * 添加地区数据
                     */
                    options3Items.add(Province_AreaList);
                }
            }
        }).start();

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public void ShowPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String province = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(options2);
                String district = options3Items.get(options1).get(options2).get(options3);

                if(province.equals(city)){
                    districtName = province + district;
                }else {
                    districtName = province + city+ district;
                }
                mBtnDistrict.setText(districtName);
                Log.e("tag",districtName);
                isDistrictSelected = true;
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
    }

}


