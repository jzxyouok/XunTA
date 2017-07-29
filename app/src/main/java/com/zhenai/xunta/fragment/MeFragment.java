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
import com.zhenai.xunta.activity.SettingActivity;
import com.zhenai.xunta.adapter.PictureGallaryAdapter;
import com.zhenai.xunta.utils.SpacesItemDecoration;
import com.zhenai.xunta.widget.ItemLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wenjing.tang on 2017/7/24.
 */

public class MeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mPictureRecyclerView;
    private View rootView;
    private PictureGallaryAdapter mAdapter;
    private List<Integer> mDatas;
    //private RecyclerView.LayoutManager mLayoutManager;

    private ItemLinearLayout mSettingItemLinearLayout;

    private ImageButton edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_me_main, container, false);

        initViews();

       /* mSettingItemLinearLayout.addOnItemClickListener(new ItemLinearLayout.OnItemLinearLayoutListener() {
            @Override
            public void onItemClick() {

            }
        });*/

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

        return rootView;
    }

    public void initViews() {
        mPictureRecyclerView = rootView.findViewById(R.id.picture_recyclerview); //得到RecyclerView控件
        mSettingItemLinearLayout = rootView.findViewById(R.id.setting);//通过findViewById得到自定义控件，需要先获取到对应的View，再通过view绑定控件
        edit = rootView.findViewById(R.id.btn_edit_self_introduction);
    }

    private void initDatas()
    {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.pic1,
                R.drawable.pic4, R.drawable.pic2, R.drawable.pic3, R.drawable.pic1, R.drawable.pic3, R.drawable.pic2));
    }

    private void setListeners() {
        mSettingItemLinearLayout.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting:

                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_edit_self_introduction:

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

}
