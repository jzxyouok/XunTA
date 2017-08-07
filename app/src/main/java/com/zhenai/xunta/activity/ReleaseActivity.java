package com.zhenai.xunta.activity;

import android.content.DialogInterface;
import android.graphics.Color;
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

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.zhenai.xunta.R;
import com.zhenai.xunta.model.JsonBean;
import com.zhenai.xunta.utils.GetJsonDataUtil;
import com.zhenai.xunta.utils.ShowToast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.zhenai.xunta.R.id.btn_release_activity;

/**
 * 发布页面
 * Created by wenjing.tang on 2017/8/7.
 */

public class ReleaseActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtActivityDescription, mEtPrice;
    private ImageButton mImgBtnBack, mImgBtnClearActivity,mImgBtnClearPlace, mImgBtnClearPrice;
    private TextView  mTvWordsCount;

    private Button mImgBtnActivity, mImgBtnPlace,  mBtnReleaseActivity;

    //城市数据源
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    String activity, activityDescription, place, price;

    List<String> activityList;
    int length;
    private static final int REQUIRED_MINIMUM_LENGTH = 6;//要求的最短长度
    private static final int REQUIRED_MAXIMUM_LENGTH = 30;//要求的最长长度

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        initViews();

        initDatas();

        setListeners();

    }

    private void initViews() {
        mEtActivityDescription = (EditText) findViewById(R.id.et_activity_description);
        mImgBtnBack = (ImageButton) findViewById(R.id.ib_title_back);
        mImgBtnActivity = (Button) findViewById(R.id.btn_activity);
        mImgBtnPlace = (Button) findViewById(R.id.btn_place);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mTvWordsCount = (TextView) findViewById(R.id.tv_words_count);

        mImgBtnClearActivity = (ImageButton) findViewById(R.id.ib_clear_activity);
        mImgBtnClearPlace = (ImageButton) findViewById(R.id.ib_clear_place);
        mImgBtnClearPrice = (ImageButton) findViewById(R.id.ib_clear_price);

        mBtnReleaseActivity = (Button) findViewById(btn_release_activity);

        activityDescription = mEtActivityDescription.getText().toString();
        length = activityDescription.length();
        mTvWordsCount.setText(length + "/" + REQUIRED_MAXIMUM_LENGTH );
    }

    private void initDatas() {

        activityList = new ArrayList<>();
        activityList.add("运动");
        activityList.add("吃喝");
        activityList.add("旅游");
        activityList.add("看电影");
        activityList.add("玩游戏");
        activityList.add("其他");

        initJsonData();

    }

    private void setListeners() {
        mImgBtnBack.setOnClickListener(this);
        mImgBtnActivity.setOnClickListener(this);
        mImgBtnPlace.setOnClickListener(this);

        mEtActivityDescription.addTextChangedListener(new TextChangeListener());
        mBtnReleaseActivity.setOnClickListener(this);

        mImgBtnClearActivity.setOnClickListener(this);
        mImgBtnClearPlace.setOnClickListener(this);
        mImgBtnClearPrice.setOnClickListener(this);
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

            case R.id.btn_activity:

                showActivityPickerView();
                break;

            case R.id.ib_clear_activity:
                break;

            case R.id.btn_place:
                showaActivityPickerView();
                break;

            case R.id.ib_clear_place:
                break;

            case R.id.btn_price:
                break;

            case R.id.ib_clear_price:
                break;

            case R.id.btn_release_activity:
                break;
        }

    }

    private void showActivityPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                activity = activityList.get(options1);
                mImgBtnActivity.setText(activity);
            }
        })
                .setTitleText("活动类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(activityList);
        pvOptions.show();



    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还未发布活动，是否退出");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.show();
    }

    public void showaActivityPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //String province = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(options2);
                String district = options3Items.get(options1).get(options2).get(options3);

                place =  city+ district;

                mImgBtnPlace.setText(place);

            }
        })
                .setTitleText("活动地点")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
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
            activityDescription = mEtActivityDescription.getText().toString();
            length = activityDescription.length();
            mTvWordsCount.setText(length + "/" +REQUIRED_MAXIMUM_LENGTH);

            if (length > REQUIRED_MAXIMUM_LENGTH){
                ShowToast.showToast("输入过多哦~");
            }

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
                String JsonData = new GetJsonDataUtil().getJson(ReleaseActivity.this,"province.json");//获取assets目录下的json文件数据
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

}
