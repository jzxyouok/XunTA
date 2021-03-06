package com.zhenai.xunta.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.zhenai.xunta.R;
import com.zhenai.xunta.model.JsonBean;
import com.zhenai.xunta.utils.GetJsonDataUtil;
import com.zhenai.xunta.utils.ServerUrl;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.utils.ShowToast;
import com.zhenai.xunta.widget.CustomPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人资料
 * Created by wenjing.tang on 2017/7/27.
 */

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener, CustomPopupWindow.OnItemClickListener {

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

    private String nickName, sex, districtName, birthDate ;
    private String phoneNumber, password;
    File outputImageFile = null;

    boolean isImageUploaded = false;
    boolean isTimeSelected = false;
    boolean isDistrictSelected = false;

    String dataResultCode, avatarResultCode;//注册的两个返回码
    public static final String DATA_UPLOAD_SUCCESS = "1701031";
    public static final String DATA_UPLOAD_FAILURE= "1701032";

    public static final String AVATAR_UPLOAD_SUCCESS = "1702001";
    public static final String AVATAR_UPLOAD_FAILURE = "1702002";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        initViews();
        setListeners();

        phoneNumber = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");

        initJsonData();
        mPop=new CustomPopupWindow(this);
        mPop.setOnItemClickListener(this);
    }

    public void initViews() {
        mLinearLayoutAvatar = (LinearLayout) findViewById(R.id.ll_upload_avatar);
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mEtNickname = (EditText) findViewById(R.id.et_set_nickname);
        mRadioBtnMale = (RadioButton) findViewById(R.id.rb_male);
        mRadioBtnFemale = (RadioButton) findViewById(R.id.rb_female);

        mBtnDistrict = (Button) findViewById(R.id.btn_select_district);
        mBtnDistrict.getBackground().setAlpha(0);

        mBtnBirthDate = (Button) findViewById(R.id.btn_select_birthdate);
        mBtnBirthDate.getBackground().setAlpha(0);
        mBtnDone = (Button) findViewById(R.id.btn_done);
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
                        birthDate = sdf.format(date);
                        mBtnBirthDate.setText(birthDate);
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

            case R.id.btn_done: //完成，提交数据到服务器、写入数据到本地，并进入主界面
                String regex = "[a-zA-Z0-9_\u4e00-\u9fa5]+";
                String regexPureNumber = "^\\d+$";
                nickName = mEtNickname.getText().toString().trim();
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postPersonalData();
                        }
                    }).start();
                }
                break;
        }
    }

    public void postPersonalData() {

        if (mRadioBtnMale.isChecked()){
             sex = "1";
        }else {
             sex = "0";
        }

        //上传基本信息
      /*  Map<String, String> personDataMap = new HashMap<>();
        personDataMap.put("phone",phoneNumber);
        personDataMap.put("password",password);
        personDataMap.put("nickname",nickName);
        personDataMap.put("sex",sex);
        personDataMap.put("birthDate",birthDate);
        personDataMap.put("birthPlace",districtName);

        HttpUtil.sendPostMultiRequestWithOkHttp(ServerUrl.REGISTER_URL, personDataMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ShowToast.showToast("服务器异常，请稍后重试~");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    dataResultCode = jsonObject.getString("resultCode");
                    Log.e("resultCode","dataResultCode:"+dataResultCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        //上传图片
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",phoneNumber)
                .addFormDataPart("type","1")
                .addFormDataPart("fileData", outputImageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), outputImageFile))
                .build();

        Request request = new Request.Builder()
                .url(ServerUrl.UPLOAD_PHOTO_URL)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                avatarResultCode = jsonObject.getString("resultCode");
                Log.e("resultCode","avatarResultCode:" + avatarResultCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        OkHttpClient client1 = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",phoneNumber);
        builder.add("password",password);
        builder.add("nickname",nickName);
        builder.add("sex",sex);
        builder.add("birthDate",birthDate);
        builder.add("birthPlace",districtName);

        RequestBody requestBody1  = builder.build();

        Request request1 = new Request.Builder()
                .url(ServerUrl.REGISTER_URL)
                .post(requestBody1)
                .build();

/*        RequestBody requestBody1 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",phoneNumber)
                .addFormDataPart("password",password)
                .addFormDataPart("nickname",nickName)
                .addFormDataPart("sex",sex)
                .addFormDataPart("birthDate",birthDate)
                .addFormDataPart("birthPlace",districtName)
                .build();*/

/*        Log.e("personData","phone:" + phoneNumber);
        Log.e("personData","password:" + password);
        Log.e("personData","nickname:" + nickName);
        Log.e("personData","sex:" + sex);
        Log.e("personData","birthDate:" + birthDate);
        Log.e("personData","districtName:" + districtName);
        Log.e("personData","imageFile:" + outputImageFile.getName());*/

/*        Request request1 = new Request.Builder()
                .url(ServerUrl.REGISTER_URL)
                .post(requestBody1)
                .build();*/

        try {
            Response response1 = client1.newCall(request1).execute();
            String responseData1 = response1.body().string();
            try {
                JSONObject jsonObject1 = new JSONObject(responseData1);
                dataResultCode = jsonObject1.getString("resultCode");
                Log.e("resultCode","dataResultCode:" + dataResultCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: 2017/8/4  判断
       if (dataResultCode.equals(DATA_UPLOAD_SUCCESS) && avatarResultCode.equals(AVATAR_UPLOAD_SUCCESS)){ //注册成功
           //保存资料（手机号等），便于下次直接登录
           SharedPreferencesUtil.setParam(PersonalDataActivity.this,"phone",phoneNumber);
           SharedPreferencesUtil.setParam(PersonalDataActivity.this,"nickName",nickName);
           SharedPreferencesUtil.setParam(PersonalDataActivity.this,"sex",sex);
           SharedPreferencesUtil.setParam(PersonalDataActivity.this,"birthDate",birthDate);
           SharedPreferencesUtil.setParam(PersonalDataActivity.this,"districtName",districtName);

           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   ShowToast.showToast("恭喜您，注册成功~");
               }
           });
           startActivity(new Intent(PersonalDataActivity.this, LoginActivity.class));//注册成功，跳转到登录页

           finish();//销毁资料填写页面

           //发广播销毁注册页面
           Intent sendBroadCastIntent  = new Intent("com.xunta.FINISH_REGISTER_ACTIVITY__BROADCAST");
           sendBroadcast(sendBroadCastIntent);

       }else if(avatarResultCode.equals(AVATAR_UPLOAD_FAILURE)){
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   ShowToast.showToast("头像上传失败");
               }
           });

       } else {  //注册失败
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   ShowToast.showToast("服务器异常，请稍后重试~");
               }
           });
       }


      /*  // TODO: 2017/8/1   向服务器POST String数据
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",phoneNumber)
                .addFormDataPart("password",password)
                .addFormDataPart("nickname",nickName)
                .addFormDataPart("sex",sex)
                .addFormDataPart("birthDate",birthDate)
                .addFormDataPart("birthPlace",districtName)
                .addFormDataPart("avatars", outputImageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), outputImageFile))
                .build();

        Request request = new Request.Builder()
                .url(ServerUrl.REGISTER_URL)
                .post(requestBody)
                .build();
        try {
            Response response =  client.newCall(request).execute();
            String responseData = response.body().string();

            JSONObject jsonObject = new JSONObject(responseData);
            resultCode = jsonObject.getString("resultCode");

            Log.e("personData","ResultCode:" + resultCode);
            Log.e("personData","phone:" + phoneNumber);
            Log.e("personData","password:" + password);
            Log.e("personData","nickname:" + nickName);
            Log.e("personData","sex:" + sex);
            Log.e("personData","birthDate:" + birthDate);
            Log.e("personData","districtName:" + districtName);
            Log.e("personData","imageFile:" + outputImageFile.getName());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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
        outputImageFile = new File(getExternalCacheDir(), "output_image.jpg");
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
            imageUri = FileProvider.getUriForFile(PersonalDataActivity.this, "com.example.cameraalbumtest.fileprovider", outputImageFile);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");//选择图片
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    ShowToast.showToast("您取消了权限~");
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
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        mLinearLayoutAvatar.setBackground(new BitmapDrawable(bitmap));
                        mTvHint.setVisibility(View.INVISIBLE);
                        isImageUploaded = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);// 4.4及以上系统使用这个方法处理图片
                    } else {
                        handleImageBeforeKitKat(data); // 4.4以下系统使用这个方法处理图片
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

        outputImageFile = new File(imagePath);

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
                //Log.e("tag",districtName);
                isDistrictSelected = true;
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
    }

}


