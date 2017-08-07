package com.zhenai.xunta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.activity.ReleaseActivity;

/**
 * 寻TA fragment
 * Created by wenjing.tang on 2017/7/25.
 */

public class FindTaFragment extends Fragment implements View.OnClickListener{

    private TextView mtvRelease;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_findta_main, container, false);

        initViews();

        setListeners();
        return rootView;


    }


    private void initViews() {

        mtvRelease = rootView.findViewById(R.id.tv_wanna_release);
    }


    private void setListeners() {

        mtvRelease.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_wanna_release:

                startActivity(new Intent(getActivity(), ReleaseActivity.class));

                break;
        }

    }

/*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        tv.setText(getArguments().getString("ARGS"));
    }*/

    public static FindTaFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FindTaFragment fragment = new FindTaFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
