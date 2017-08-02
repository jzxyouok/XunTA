package com.zhenai.xunta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.zhenai.xunta.R;
import com.zhenai.xunta.activity.CustomizedDisplayActivity;
import com.zhenai.xunta.activity.EditSelfDataActivity;
import com.zhenai.xunta.activity.MemberCenterActivity;
import com.zhenai.xunta.activity.MyAppointmentActivity;
import com.zhenai.xunta.activity.MyCertificationActivity;
import com.zhenai.xunta.activity.MyChatCouponActivity;
import com.zhenai.xunta.activity.MyFocusActivity;
import com.zhenai.xunta.activity.MyReleaseActivity;
import com.zhenai.xunta.activity.SettingActivity;
import com.zhenai.xunta.adapter.ImagePickerAdapter;
import com.zhenai.xunta.presenter.MeFragmentPresenter;
import com.zhenai.xunta.utils.GlideImageLoader;
import com.zhenai.xunta.utils.SharedPreferencesUtil;
import com.zhenai.xunta.view.IMeFragmentView;
import com.zhenai.xunta.widget.ItemLinearLayout;
import com.zhenai.xunta.widget.SelectDialog;

import java.util.ArrayList;
import java.util.List;

import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * Created by wenjing.tang on 2017/7/24.
 */

public class MeFragment extends Fragment implements IMeFragmentView, View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener{

    private ItemLinearLayout mMyReleaseItemLinearLayout, mMyAppointmentItemLinearLayout, mMyFocusItemLinearLayout, mMyBlackListItemLinearLayout, mMemberCenterItemLinearLayout,
            mMyChatCouponItemLinearLayout, mCustomizedDisplayItemLinearLayout, mMyCertificationItemLinearLayout, mSettingItemLinearLayout;

    private TextView mTvNickname, mTvUserID;
    private ImageButton edit;
    private ExpandableTextView mExpandableTextViewInduction;

    View rootView;

    MeFragmentPresenter meFragmentPresenter = new MeFragmentPresenter(this, getActivity());

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selectImageList; //当前选择的所有图片
    private static final int MAX_IMG_COUNT = 9;  //允许选择图片最大数

    private boolean isSelfIntroductionEdited = false; //是否编辑个人介绍

    private String phone, nickName, userID, selfIntroduction;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_me_main, container, false);

        initViews();

        initDatas();

        setListeners();

        initImagePicker();

        initWidget();

        return rootView;
    }



    public void initViews() {

        mTvNickname  = rootView.findViewById(R.id.tv_nickname);
        mTvUserID = rootView.findViewById(R.id.tv_user_id);

        mMyReleaseItemLinearLayout = rootView.findViewById(R.id.my_release);
        mMyAppointmentItemLinearLayout = rootView.findViewById(R.id.my_appointment);
        mMyFocusItemLinearLayout= rootView.findViewById(R.id.my_focus);
        mMyBlackListItemLinearLayout = rootView.findViewById(R.id.my_blaklist);
        mMemberCenterItemLinearLayout = rootView.findViewById(R.id.member_center);
        mMyChatCouponItemLinearLayout = rootView.findViewById(R.id.my_chat_coupon);
        mCustomizedDisplayItemLinearLayout = rootView.findViewById(R.id.customized_display);
        mMyCertificationItemLinearLayout = rootView.findViewById(R.id.my_certification);
        mSettingItemLinearLayout = rootView.findViewById(R.id.setting);//通过findViewById得到自定义控件，需要先获取到对应的View，再通过view绑定控件

        edit = rootView.findViewById(R.id.btn_edit_self_introduction);

        mExpandableTextViewInduction = rootView.findViewById(R.id.etv_self_introduction);
        mExpandableTextViewInduction.setText("这是我的内心独白1这是我的内心独白2这是我的内心独白3");
    }

    private void setListeners() {
        edit.setOnClickListener(this);
        mMyReleaseItemLinearLayout.setOnClickListener(this);
        mMyAppointmentItemLinearLayout.setOnClickListener(this);
        mMyFocusItemLinearLayout.setOnClickListener(this);
        mMyBlackListItemLinearLayout.setOnClickListener(this);
        mMemberCenterItemLinearLayout.setOnClickListener(this);
        mMyChatCouponItemLinearLayout.setOnClickListener(this);
        mCustomizedDisplayItemLinearLayout.setOnClickListener(this);
        mMyCertificationItemLinearLayout.setOnClickListener(this);
        mMyCertificationItemLinearLayout.setOnClickListener(this);
        mSettingItemLinearLayout.setOnClickListener(this);
    }

    private void initDatas() {
        phone = (String) SharedPreferencesUtil.getParam(getActivity(),"phone","");
        nickName = (String) SharedPreferencesUtil.getParam(getActivity(),"nickName","");
        userID =  (String) SharedPreferencesUtil.getParam(getActivity(),"userID","");
        selfIntroduction = (String) SharedPreferencesUtil.getParam(getActivity(),"selfIntroduction","");
        if ( (nickName.equals("")) || (userID.equals("")) || (selfIntroduction.equals("")) ){//本地没有数据，请求服务器
            // TODO: 2017/8/2 向服务器请求数据
        }else {
            mTvNickname.setText(nickName);
            mTvUserID.setText(userID);
            mExpandableTextViewInduction.setText(selfIntroduction);
        }

        // TODO: 2017/8/2 头像和照片
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit_self_introduction:
                toEditSelfIntroductionActivity();
                break;

            case R.id.my_release:
                toMyReleaseActivity();
                break;

            case R.id.my_appointment:
                toMyAppointmentActivity();
                break;

            case R.id.my_focus:
                toMyFocusActivity();
                break;
            case R.id.my_blaklist:
               // toMyFocusActivity();
                break;

            case R.id.member_center:
                toMemberCenterActivity();
                break;

            case R.id.my_chat_coupon:
                toMyChatCouponActivity();
                break;

            case R.id.customized_display:
                toCustomizedDisplayActivity();
                break;

            case R.id.my_certification:
                toMyCertificationActivity();
                break;

            case R.id.setting:
                toSettingActivity();
                break;


        }
    }

    public static FindTaFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FindTaFragment fragment = new FindTaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void toEditSelfIntroductionActivity() {
        startActivity(new Intent(getActivity(), EditSelfDataActivity.class));
    }

    @Override
    public void toMyReleaseActivity() {
        startActivity(new Intent(getActivity(), MyReleaseActivity.class));
    }

    @Override
    public void toMyAppointmentActivity() {
        startActivity(new Intent(getActivity(), MyAppointmentActivity.class));
    }

    @Override
    public void toMyFocusActivity() {
        startActivity(new Intent(getActivity(), MyFocusActivity.class));
    }

    @Override
    public void toMemberCenterActivity() {
        startActivity(new Intent(getActivity(), MemberCenterActivity.class));
    }

    @Override
    public void toMyChatCouponActivity() {
        startActivity(new Intent(getActivity(), MyChatCouponActivity.class));
    }

    @Override
    public void toCustomizedDisplayActivity() {
        startActivity(new Intent(getActivity(), CustomizedDisplayActivity.class));
    }

    @Override
    public void toMyCertificationActivity() {
        startActivity(new Intent(getActivity(), MyCertificationActivity.class));
    }

    @Override
    public void toSettingActivity() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(MAX_IMG_COUNT);              //选中数量限制
        imagePicker.setMultiMode(true);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = rootView.findViewById(R.id.picture_recyclerview);
        selectImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(getActivity(), selectImageList, MAX_IMG_COUNT);
        adapter.setOnItemClickListener(this);

        LinearLayoutManager manager =  new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style.transparentFrameWindowStyle, listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(MAX_IMG_COUNT - selectImageList.size());
                                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(MAX_IMG_COUNT - selectImageList.size());
                                Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开图片预览
                Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selectImageList.addAll(images);
                    adapter.setImages(selectImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selectImageList.clear();
                    selectImageList.addAll(images);
                    adapter.setImages(selectImageList);
                }
            }
        }
    }

}
