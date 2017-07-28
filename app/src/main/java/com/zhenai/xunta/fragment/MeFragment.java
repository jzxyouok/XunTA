package com.zhenai.xunta.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhenai.xunta.R;
import com.zhenai.xunta.adapter.PictureGallaryAdapter;
import com.zhenai.xunta.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wenjing.tang on 2017/7/24.
 */

public class MeFragment extends Fragment {

    private RecyclerView mPictureRecyclerView;
    View rootView;
    private PictureGallaryAdapter mAdapter;
    private List<Integer> mDatas;
    //private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_me_main, container, false);

        initViews();
        initDatas();

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
        //得到RecyclerView控件
        mPictureRecyclerView = rootView.findViewById(R.id.picture_recyclerview);
    }

    private void initDatas()
    {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.pic1,
                R.drawable.pic4, R.drawable.pic2, R.drawable.pic3, R.drawable.pic1, R.drawable.pic3, R.drawable.pic2));
    }

    public static FindTaFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FindTaFragment fragment = new FindTaFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
