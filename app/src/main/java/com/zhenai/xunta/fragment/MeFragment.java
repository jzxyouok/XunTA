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
import android.widget.ImageButton;

import com.zhenai.xunta.R;
import com.zhenai.xunta.activity.CustomizedDisplayActivity;
import com.zhenai.xunta.activity.EditSelfDataActivity;
import com.zhenai.xunta.activity.MemberCenterActivity;
import com.zhenai.xunta.activity.MyAppointmentActivity;
import com.zhenai.xunta.activity.MyCertificationActivity;
import com.zhenai.xunta.activity.MyChatCouponActivity;
import com.zhenai.xunta.activity.MyFocusActivity;
import com.zhenai.xunta.activity.MyReleaseActivity;
import com.zhenai.xunta.activity.PhotoViewActivity;
import com.zhenai.xunta.activity.SettingActivity;
import com.zhenai.xunta.adapter.PictureGallaryAdapter;
import com.zhenai.xunta.presenter.MeFragmentPresenter;
import com.zhenai.xunta.utils.SpacesItemDecoration;
import com.zhenai.xunta.view.IMeFragmentView;
import com.zhenai.xunta.widget.ItemLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * Created by wenjing.tang on 2017/7/24.
 */

public class MeFragment extends Fragment implements IMeFragmentView, View.OnClickListener{

    private RecyclerView mPictureRecyclerView;
    private View rootView;
    private PictureGallaryAdapter mAdapter;
    private List<Integer> mDatas;
    //private RecyclerView.LayoutManager mLayoutManager;

    private ItemLinearLayout mMyReleaseItemLinearLayout, mMyAppointmentItemLinearLayout, mMyFocusItemLinearLayout, mMemberCenterItemLinearLayout,
            mMyChatCouponItemLinearLayout, mCustomizedDisplayItemLinearLayout, mMyCertificationItemLinearLayout, mSettingItemLinearLayout;

    private ImageButton edit;
    private ExpandableTextView mExpandableTextViewInduction;

    MeFragmentPresenter meFragmentPresenter = new MeFragmentPresenter(this, getActivity());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_me_main, container, false);

        initViews();

        initDatas();

        setListeners();

        //设置布局管理器
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPictureRecyclerView.setLayoutManager(mLayoutManager);

        //设置适配器
        mAdapter = new PictureGallaryAdapter(getActivity(), mDatas);
        mPictureRecyclerView.addItemDecoration(new SpacesItemDecoration(10));//设置item间隔
        mPictureRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickLitener(new PictureGallaryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                intent.putIntegerArrayListExtra("image_data", (ArrayList<Integer>) mDatas);
                intent.putExtra("index",position);

                startActivity(intent);
            }

        });
        return rootView;
    }

    public void initViews() {

        mMyReleaseItemLinearLayout = rootView.findViewById(R.id.my_release);
        mMyAppointmentItemLinearLayout = rootView.findViewById(R.id.my_appointment);
        mMyFocusItemLinearLayout= rootView.findViewById(R.id.my_focus);
        mMemberCenterItemLinearLayout = rootView.findViewById(R.id.member_center);
        mMyChatCouponItemLinearLayout = rootView.findViewById(R.id.my_chat_coupon);
        mCustomizedDisplayItemLinearLayout = rootView.findViewById(R.id.customized_display);
        mMyCertificationItemLinearLayout = rootView.findViewById(R.id.my_certification);
        mSettingItemLinearLayout = rootView.findViewById(R.id.setting);//通过findViewById得到自定义控件，需要先获取到对应的View，再通过view绑定控件

        mPictureRecyclerView = rootView.findViewById(R.id.picture_recyclerview); //得到RecyclerView控件
        edit = rootView.findViewById(R.id.btn_edit_self_introduction);
        mExpandableTextViewInduction = rootView.findViewById(R.id.etv_self_introduction);
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.pic1, R.drawable.pic4, R.drawable.pic2,
                                  R.drawable.pic3, R.drawable.pic1, R.drawable.pic3, R.drawable.pic2));

        mExpandableTextViewInduction.setText("这是我的内心独白1");
    }

    private void setListeners() {
        edit.setOnClickListener(this);
        mMyReleaseItemLinearLayout.setOnClickListener(this);
        mMyAppointmentItemLinearLayout.setOnClickListener(this);
        mMyFocusItemLinearLayout.setOnClickListener(this);
        mMemberCenterItemLinearLayout.setOnClickListener(this);
        mMyChatCouponItemLinearLayout.setOnClickListener(this);
        mCustomizedDisplayItemLinearLayout.setOnClickListener(this);
        mMyCertificationItemLinearLayout.setOnClickListener(this);
        mMyCertificationItemLinearLayout.setOnClickListener(this);
        mSettingItemLinearLayout.setOnClickListener(this);
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
}
