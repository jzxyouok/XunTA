package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.utils.ShowToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.zhenai.xunta.R.id.imgBtn_self_introduction;

/**
 * 编辑个人资料页
 * Created by wenjing.tang on 2017/8/1.
 */

public class EditSelfDataActivity extends BaseActivity implements View.OnClickListener {

    private  ImageButton mImgBtnBack;
    private TextView mTvSaveData;

    private TextView mTvNickName, mTvSex, mTvBirthDate, mTvDistrictName;
    private String nickName,  sex, birthDate, districtName; //个人基本信息

    private TextView mTvSelfIntroductionStatus; //自我介绍
    private ImageButton mImgBtnSelfIntroduction;
    private String selfIntroduction;
    private static final  int  SELF_INRTODUCTION_REQUEST_CODE = 1;

    private ArrayList<String> marriageStatusList = new ArrayList<>(); //感情状况
    String marriageStatus;
    private ImageButton mImgBtnMarriageStatus;
    private TextView mTvMarriageStatus;

    private ArrayList<String> professions1Items = new ArrayList<>();   //职业
    private ArrayList<ArrayList<String>> professions2Items = new ArrayList<>();
    String profession;
    private ImageButton mImgBtnProfession;
    private TextView mTvIsProfession;

    private ArrayList<String> isSmokingList = new ArrayList<>();//是否吸烟
    String isSmoking;
    private ImageButton mImgBtnIsSmoking;
    private TextView mTvIsSmoking;

    private ArrayList<String> isDrinkingList = new ArrayList<>();//是否喝酒
    String isDrinking;
    private ImageButton mImgBtnIsDrinking;
    private TextView mTvIsDrinking;

    private ArrayList<String> hobbyList = new ArrayList<>();//兴趣爱好
    String hobby;
    private ImageButton mImgBtnHobby;
    private TextView mTvHobby;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_data);

        initViews();

        initDatas();

        setListeners();

    }

    private void initViews() {

        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        mTvSaveData = (TextView) findViewById(R.id.tv_save_my_data);

        mTvNickName = (TextView) findViewById(R.id.tv_edit_nickname);
        mTvSex = (TextView) findViewById(R.id.tv_edit_nickname);
        mTvBirthDate = (TextView) findViewById(R.id.tv_edit_birthDate);
        mTvDistrictName= (TextView) findViewById(R.id.tv_edit_district);

        mTvSelfIntroductionStatus = (TextView) findViewById(R.id.tv_self_introduction_status);
        mImgBtnSelfIntroduction = (ImageButton) findViewById(imgBtn_self_introduction);

        mImgBtnMarriageStatus = (ImageButton) findViewById(R.id.imgBtn_relationship_status);
        mTvMarriageStatus = (TextView) findViewById(R.id.tv_relationship_status);

        mImgBtnProfession = (ImageButton) findViewById(R.id.imgBtn_profession_status);
        mTvIsProfession = (TextView) findViewById(R.id.tv_profession_status);

        mImgBtnIsSmoking = (ImageButton) findViewById(R.id.imgBtn_is_smoking);
        mTvIsSmoking = (TextView) findViewById(R.id.tv_is_smoking_status);

        mImgBtnIsDrinking = (ImageButton) findViewById(R.id.imgBtn_is_drinking);
        mTvIsDrinking = (TextView) findViewById(R.id.tv_is_drinking_status);

        mImgBtnHobby = (ImageButton) findViewById(R.id.imgBtn_hobby);
        mTvHobby= (TextView) findViewById(R.id.tv_hobby_status);
    }

    private void initDatas() {
        //取4项 个人信息
        nickName = (String) SharedPreferencesUtil.getParam(this,"nickName","");
        sex = (String) SharedPreferencesUtil.getParam(this,"phone","");
        birthDate = (String) SharedPreferencesUtil.getParam(this,"birthDate","");
        districtName =  (String) SharedPreferencesUtil.getParam(this,"districtName","");

        selfIntroduction = (String) SharedPreferencesUtil.getParam(this,"selfIntroduction","");
        marriageStatus = (String) SharedPreferencesUtil.getParam(this,"marriageStatus","");
        profession = (String) SharedPreferencesUtil.getParam(this,"profession","");
        isSmoking = (String) SharedPreferencesUtil.getParam(this,"isSmoking","");
        isDrinking = (String) SharedPreferencesUtil.getParam(this,"isDrinking","");
        hobby = (String) SharedPreferencesUtil.getParam(this,"hobby","");
        if ( (nickName.equals("") || null == nickName) && (selfIntroduction.equals("") || null == selfIntroduction) ){

            // TODO: 2017/8/3  本地没有缓存 个人信息 + 交友信息 数据，就从服务器请求 个人信息 + 交友信息 的数据 并设置到TextView中

        }else {
            mTvNickName.setText(nickName);
            mTvSex.setText(sex);
            mTvBirthDate.setText(birthDate);
            mTvDistrictName.setText(districtName);

            mTvSelfIntroductionStatus.setText(selfIntroduction);
            mTvMarriageStatus.setText(marriageStatus);
            mTvIsProfession.setText(profession);
            mTvIsSmoking.setText(isSmoking);
            mTvIsDrinking.setText(isDrinking);
            mTvHobby.setText(hobby);
        }

        //感情状况
        marriageStatusList.add("单身");
        marriageStatusList.add("恋爱");
        marriageStatusList.add("已婚");
        marriageStatusList.add("离异");
        marriageStatusList.add("保密");
        //是否抽烟
        isSmokingList.add("不抽烟");
        isSmokingList.add("稍微抽一点烟");
        isSmokingList.add("烟抽得很多");
        isSmokingList.add("只在社交场合会抽烟");
        //是否喝酒
        isDrinkingList.add("不喝酒");
        isDrinkingList.add("稍微喝一点酒");
        isDrinkingList.add("酒喝得很多");
        isDrinkingList.add("只在社交场合会喝酒");

        //职业，选项1
        professions1Items.add("销售");
        professions1Items.add("IT");
        professions1Items.add("电子/通信");
        professions1Items.add("生产/制造");
        professions1Items.add("人事/行政");
        professions1Items.add("金融/银行/保险");
        professions1Items.add("建筑/房地产");

        //职业，选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("销售总监");
        options2Items_01.add("销售经理");
        options2Items_01.add("销售主管");
        options2Items_01.add("销售专员");
        options2Items_01.add("客户经理");
        options2Items_01.add("客户代表");
        options2Items_01.add("其他");

        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("IT技术总监");
        options2Items_02.add("IT技术经理");
        options2Items_02.add("IT工程师");
        options2Items_02.add("测试专员");
        options2Items_02.add("网页设计");
        options2Items_02.add("其他");

        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("通信技术");
        options2Items_03.add("电子技术");
        options2Items_03.add("其他");

        ArrayList<String> options2Items_04 = new ArrayList<>();
        options2Items_04.add("工厂经理");
        options2Items_04.add("工程师");
        options2Items_04.add("项目主管");
        options2Items_04.add("生产领班");
        options2Items_04.add("操作工人");
        options2Items_04.add("安全员");
        options2Items_04.add("其他");

        ArrayList<String> options2Items_05 = new ArrayList<>();
        options2Items_05.add("人事总监");
        options2Items_05.add("人事经理");
        options2Items_05.add("人事主管");
        options2Items_05.add("人事专员");
        options2Items_05.add("招聘经理");
        options2Items_05.add("招聘专员");
        options2Items_05.add("培训经理");
        options2Items_05.add("培训专员");
        options2Items_05.add("秘书");
        options2Items_05.add("文员");
        options2Items_05.add("其他");

        ArrayList<String> options2Items_06 = new ArrayList<>();
        options2Items_06.add("投资");
        options2Items_06.add("保险");
        options2Items_06.add("金融");
        options2Items_06.add("银行");
        options2Items_06.add("证券");
        options2Items_06.add("其他");

        ArrayList<String> options2Items_07 = new ArrayList<>();
        options2Items_07.add("建造师");
        options2Items_07.add("工程师");
        options2Items_07.add("规划师");
        options2Items_07.add("景观设计");
        options2Items_07.add("房地产策划");
        options2Items_07.add("房地产交易");
        options2Items_07.add("物业管理");
        options2Items_07.add("其他");

        professions2Items.add(options2Items_01);
        professions2Items.add(options2Items_02);
        professions2Items.add(options2Items_03);
        professions2Items.add(options2Items_04);
        professions2Items.add(options2Items_05);
        professions2Items.add(options2Items_06);
        professions2Items.add(options2Items_07);
        /*--------职业数据源添加完毕---------*/

        hobbyList.add("美食烹饪");
        hobbyList.add("摄影");
        hobbyList.add("电影");
        hobbyList.add("阅读");
        hobbyList.add("旅游");
        hobbyList.add("健身");
        hobbyList.add("游泳");
        hobbyList.add("网球");
        hobbyList.add("羽毛球");
        hobbyList.add("跑步");
        hobbyList.add("高尔夫");
        hobbyList.add("马术");
        hobbyList.add("极限运动");
        hobbyList.add("钓鱼");
    }

    private void setListeners() {
        mImgBtnBack.setOnClickListener(this);
        mTvSaveData.setOnClickListener(this);

        mImgBtnSelfIntroduction.setOnClickListener(this);
        mImgBtnMarriageStatus.setOnClickListener(this);
        mImgBtnIsSmoking.setOnClickListener(this);
        mImgBtnIsDrinking.setOnClickListener(this);
        mImgBtnProfession.setOnClickListener(this);
        mImgBtnHobby.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_back: //返回键
                showDialog();
                break;

            case R.id.tv_save_my_data:
                saveData();
                ShowToast.showToast("保存成功");
                finish();
                break;

            case R.id.imgBtn_self_introduction:
                Intent intent = new Intent(EditSelfDataActivity.this, EditSelfIntroductionActivity.class);
                intent.putExtra("selfIntroduction",selfIntroduction);
                startActivityForResult(intent, SELF_INRTODUCTION_REQUEST_CODE);
                break;

            case R.id.imgBtn_relationship_status:
               showMarriageStatusPickerView();
                break;

            case R.id.imgBtn_profession_status:
                showProfessionPickerView();
                break;

            case R.id.imgBtn_is_smoking:
                showIsSmokingPickerView();
                break;

            case R.id.imgBtn_is_drinking:
                showIsDrinkingPickerView();
                break;

            case R.id.imgBtn_hobby:
                showHobbyPickerView();
                break;
        }

    }

    //获取自我介绍
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 2017/8/2 保存接收的个人资料
        switch (requestCode){
            case SELF_INRTODUCTION_REQUEST_CODE:
                if (RESULT_OK == resultCode ){
                    selfIntroduction = data.getStringExtra("selfIntroduction"); //返回的个人介绍
                    mTvSelfIntroductionStatus.setText(selfIntroduction);
                    Log.i("selfIntroduction",selfIntroduction);

                }

                break;
        }
    }

    //保存数据到本地，并同时上传到服务器
    public void saveData(){
        Map<String,String> datingIngormationMap = new HashMap<>(); //个人信息map
        if (!mTvSelfIntroductionStatus.getText().equals("未输入")){
            SharedPreferencesUtil.setParam(this,"selfIntroduction",selfIntroduction);
            datingIngormationMap.put("selfIntroduction",selfIntroduction);
        }else {
            SharedPreferencesUtil.setParam(this,"selfIntroduction","未输入");
            datingIngormationMap.put("selfIntroduction","未输入");
        }
        if (!mTvMarriageStatus.getText().equals("未设置")){
            SharedPreferencesUtil.setParam(this,"marriageStatus",marriageStatus);
            datingIngormationMap.put("marriageStatus",marriageStatus);
        }else {
            SharedPreferencesUtil.setParam(this,"marriageStatus","未设置");
            datingIngormationMap.put("marriageStatus","未设置");
        }
        if (!mTvIsProfession.getText().equals("未设置")){
            SharedPreferencesUtil.setParam(this,"profession",profession);
            datingIngormationMap.put("profession",profession);
        }else {
            SharedPreferencesUtil.setParam(this,"profession","未设置");
            datingIngormationMap.put("profession","未设置");
        }
        if (!mTvIsSmoking.getText().equals("未设置")){
            SharedPreferencesUtil.setParam(this,"isSmoking",isSmoking);
            datingIngormationMap.put("isSmoking",isSmoking);
        }else {
            SharedPreferencesUtil.setParam(this,"isSmoking","未设置");
            datingIngormationMap.put("isSmoking","未设置");
        }
        if (!mTvIsDrinking.getText().equals("未设置")){
            SharedPreferencesUtil.setParam(this,"isDrinking",isDrinking);
            datingIngormationMap.put("isDrinking",isDrinking);
        }else {
            SharedPreferencesUtil.setParam(this,"isDrinking","未设置");
            datingIngormationMap.put("isDrinking","未设置");
        }
        if (!mTvHobby.getText().equals("未设置")){
            SharedPreferencesUtil.setParam(this,"hobby",hobby);
            datingIngormationMap.put("hobby",hobby);
        }else {
            SharedPreferencesUtil.setParam(this,"hobby","未设置");
            datingIngormationMap.put("hobby","未设置");
        }
        // TODO: 2017/8/3 将Map中的数据更新到服务器端

    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("要保存个人资料吗?");
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
                saveData();
                finish();
            }
        });
        builder.show();
    }
    public void showMarriageStatusPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                marriageStatus = marriageStatusList.get(options1);
                mTvMarriageStatus.setText(marriageStatus);
            }
        })
                .setTitleText("感情状况")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(marriageStatusList);
        pvOptions.show();
    }
    public void showProfessionPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String profession1 = professions1Items.get(options1);
                String profession2= professions2Items.get(options1).get(options2);
                profession = profession1 + ","+profession2;
                mTvIsProfession.setText(profession);

            }
        })
                .setTitleText("职业")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(professions1Items, professions2Items);//三级选择器
        pvOptions.show();
    }

    public void showIsSmokingPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                isSmoking = isSmokingList.get(options1);
                mTvIsSmoking.setText(isSmoking);
            }
        })
                .setTitleText("是否抽烟")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(isSmokingList);
        pvOptions.show();
    }

    public void showIsDrinkingPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                isDrinking = isDrinkingList.get(options1);
                mTvIsDrinking.setText(isDrinking);
            }
        })
                .setTitleText("是否喝酒")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(isDrinkingList);
        pvOptions.show();
    }


    public void showHobbyPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                hobby = hobbyList.get(options1);
                mTvHobby.setText(hobby);
            }
        })
                .setTitleText("兴趣爱好")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(hobbyList);
        pvOptions.show();
    }
}
