package com.zhenai.xunta.presenter;

import android.content.Context;

import com.zhenai.xunta.view.IMeFragmentView;

/**
 * Created by wenjing.tang on 2017/8/1.
 */

public class MeFragmentPresenter{

    IMeFragmentView meFragmentView;
    Context mContext;

    public MeFragmentPresenter(IMeFragmentView meFragmentView, Context mContext) {
        this.meFragmentView = meFragmentView;
        this.mContext = mContext;

    }



}
