package com.zhenai.xunta.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.SharedPreferencesUtil;

import java.util.ArrayList;

import static com.zhenai.xunta.R.id.imgBtn_self_introduction;

/**
 * 编辑个人资料页
 * Created by wenjing.tang on 2017/8/1.
 */

public class EditSelfDataActivity extends BaseActivity implements View.OnClickListener {

    private  ImageButton mImgBtnBack;
    private TextView mTvSaveData;

    private TextView mTvNickName, mTvSex, mTvBirthDate, mTvDistrictName;
    private String nickName,  sex, birthDate, districtName, selfIntroduction;


    private ImageButton mImgBtnSelfIntroduction;
    private static final  int  SELF_INRTODUCTION_REQUEST_CODE = 1;

    private ArrayList<String> marriageStatusList = new ArrayList<>(); //感情状况
    String marriageStatus;
    private ImageButton mImgBtnMarriageStatus;
    private TextView mTvMarriageStatus;

    private ArrayList<String> professions1Items = new ArrayList<>();//职业
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_data);

        initViews();

        initDatas();

        setListeners();

    }

    private void initViews() {
        mTvNickName = (TextView) findViewById(R.id.tv_edit_nickname);
        mTvSex = (TextView) findViewById(R.id.tv_edit_nickname);
        mTvBirthDate = (TextView) findViewById(R.id.tv_edit_birthDate);
        mTvDistrictName= (TextView) findViewById(R.id.tv_edit_district);

        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        mTvSaveData = (TextView) findViewById(R.id.tv_save_my_data);

        mImgBtnSelfIntroduction = (ImageButton) findViewById(imgBtn_self_introduction);

        mImgBtnMarriageStatus = (ImageButton) findViewById(R.id.imgBtn_relationship_status);
        mTvMarriageStatus = (TextView) findViewById(R.id.tv_relationship_status);

        mImgBtnIsSmoking = (ImageButton) findViewById(R.id.imgBtn_is_smoking);
        mTvIsSmoking = (TextView) findViewById(R.id.tv_is_smoking_status);

        mImgBtnIsDrinking = (ImageButton) findViewById(R.id.imgBtn_is_drinking);
        mTvIsDrinking = (TextView) findViewById(R.id.tv_is_drinking_status);

        mImgBtnProfession = (ImageButton) findViewById(R.id.imgBtn_profession_status);
        mTvIsProfession = (TextView) findViewById(R.id.tv_profession_status);
    }

    private void initDatas() {
        nickName = (String) SharedPreferencesUtil.getParam(this,"nickName","");
        sex = (String) SharedPreferencesUtil.getParam(this,"phone","");
        birthDate = (String) SharedPreferencesUtil.getParam(this,"birthDate","");
        districtName =  (String) SharedPreferencesUtil.getParam(this,"districtName","");

        if (nickName.equals("") || sex.equals("") || birthDate.equals("") || districtName.equals("")){
            // TODO: 2017/8/2  本地没有缓存数据，向服务器请求数据
        }else {
            mTvNickName.setText(nickName);
            mTvSex.setText(sex);
            mTvBirthDate.setText(birthDate);
            mTvDistrictName.setText(districtName);
        }
        selfIntroduction = (String) SharedPreferencesUtil.getParam(this,"selfIntroduction","");

        marriageStatusList.add("单身");
        marriageStatusList.add("恋爱");
        marriageStatusList.add("已婚");
        marriageStatusList.add("离异");
        marriageStatusList.add("保密");

        isSmokingList.add("不抽烟");
        isSmokingList.add("稍微抽一点烟");
        isSmokingList.add("烟抽得很多");
        isSmokingList.add("只在社交场合会抽烟");

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
    }

    private void setListeners() {
        mImgBtnBack.setOnClickListener(this);
        mTvSaveData.setOnClickListener(this);

        mImgBtnSelfIntroduction.setOnClickListener(this);
        mImgBtnMarriageStatus.setOnClickListener(this);
        mImgBtnIsSmoking.setOnClickListener(this);
        mImgBtnIsDrinking.setOnClickListener(this);
        mImgBtnProfession.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtn_back:
                finish();
                break;

            case R.id.tv_save_my_data:
                finish();
                break;

            case R.id.imgBtn_self_introduction:
                //finish();
                Intent intent = new Intent(EditSelfDataActivity.this, EditSelfIntroductionActivity.class);
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
        }

    }

    //获取自我介绍
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 2017/8/2 保存接收的个人资料 
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
}
