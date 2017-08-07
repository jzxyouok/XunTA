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

import com.zhenai.xunta.R;

/**
 * Created by wenjing.tang on 2017/7/25.
 */

public class RankFragment extends Fragment{

    private RadioGroup mRgRank;
    private RadioButton mRbOnedayPartner, mRbAccompanyActivityFragment;
    private View rootView;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rank_main, container, false);

        mRgRank = rootView.findViewById(R.id.rg_rank);
        mRbOnedayPartner = rootView.findViewById(R.id.rb_one_day_partner);
        mRbAccompanyActivityFragment = rootView.findViewById(R.id.rb_accompany_activity);

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
    }
}
