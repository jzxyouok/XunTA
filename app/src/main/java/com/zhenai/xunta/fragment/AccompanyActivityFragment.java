package com.zhenai.xunta.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhenai.xunta.R;

/**
 * 排行榜 -- 陪伴活动 fragment
 * Created by wenjing.tang on 2017/8/5.
 */

public class AccompanyActivityFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accompany_activity, container, false);
    }

    public AccompanyActivityFragment() {
        // Required empty public constructor
    }
}
