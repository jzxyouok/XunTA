package com.zhenai.xunta.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhenai.xunta.R;
import com.zhenai.xunta.utils.BannerImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.tang on 2017/7/25.
 */

public class RankFragment extends Fragment{

    private RadioGroup mRgRank;
    private RadioButton mRbOnedayPartner, mRbAccompanyActivityFragment;
    private View rootView;
    private Banner banner;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rank_main, container, false);

        mRgRank = rootView.findViewById(R.id.rg_rank);
        mRbOnedayPartner = rootView.findViewById(R.id.rb_one_day_partner);
        mRbAccompanyActivityFragment = rootView.findViewById(R.id.rb_accompany_activity);

        banner = rootView.findViewById(R.id.rank_banner);
        setBanner(); //设置banner

        manager = getActivity().getSupportFragmentManager();
        mRgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                transaction = manager.beginTransaction();
                switch (i){
                    case R.id.rb_one_day_partner:
                        transaction.replace(R.id.framelayout_rank_content, new OnedayPartnerFragment());
                        break;

                    case R.id.rb_accompany_activity:
                        transaction.replace(R.id.framelayout_rank_content, new AccompanyActivityFragment());
                        break;
                }
                transaction.commit();

            }
        });

        return rootView;
    }

    private void setBanner() {

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.pic2);
        imageList.add(R.drawable.pic3);
        imageList.add(R.drawable.pic4);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(imageList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public static FindTaFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FindTaFragment fragment = new FindTaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRgRank.check(R.id.rb_one_day_partner);

        banner.startAutoPlay(); //banner开始轮播
    }

    @Override
    public void onStop() {
        super.onStop();

        banner.stopAutoPlay(); //banner结束轮播
    }
}
