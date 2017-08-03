package com.zhenai.xunta.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zhenai.xunta.R;
import com.zhenai.xunta.fragment.FindTaFragment;
import com.zhenai.xunta.fragment.MeFragment;
import com.zhenai.xunta.fragment.MessageFragment;
import com.zhenai.xunta.fragment.RankFragment;

import java.util.ArrayList;

/**
 * Created by wenjing.tang on 2017/7/25.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private ArrayList<Fragment> fragments;
    Fragment mFindTaFragment, mRankFragment, mMessageFragment, mMeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tab_findta_normal, "寻TA").setActiveColorResource(R.color.lightBlue))
                .addItem(new BottomNavigationItem(R.mipmap.tab_rank_normal, "排行榜").setActiveColorResource(R.color.lightBlue))
                .addItem(new BottomNavigationItem(R.mipmap.tab_message_normal, "消息").setActiveColorResource(R.color.lightBlue))
                .addItem(new BottomNavigationItem(R.mipmap.tab_me_normal, "我的").setActiveColorResource(R.color.lightBlue))
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private ArrayList<Fragment> getFragments() {
        fragments = new ArrayList<>();
/*        fragments.add(FindTaFragment.newInstance("寻TA"));
        fragments.add(RankFragment.newInstance("排行榜"));
        fragments.add(MessageFragment.newInstance("消息"));
        fragments.add(MeFragment.newInstance("我"));*/
        mFindTaFragment = new FindTaFragment();
        mRankFragment = new RankFragment();
        mMessageFragment = new MessageFragment();
        mMeFragment = new MeFragment();

        fragments.add(mFindTaFragment);
        fragments.add(mRankFragment);
        fragments.add(mMessageFragment);
        fragments.add(mMeFragment);

        return fragments;
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //transaction.replace(R.id.layout_frame, FindTaFragment.newInstance("寻TA"));
        transaction.replace(R.id.layout_frame, mFindTaFragment);
        transaction.commit();
    }

/*    @Override
    public void onTabSelected(int position) {

        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Fragment fragment = fragments.get(position);
                    if (fragment.isAdded()) {
                        ft.replace(R.id.layout_frame, fragment);
                } else {
                    ft.add(R.id.layout_frame, fragment);
                }
                ft.commitAllowingStateLoss();
                //  ft.commit();
            }
        }

    }*/

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (fragments == null) {
                    mFindTaFragment = FindTaFragment.newInstance("寻TA");
                }
                transaction.replace(R.id.layout_frame, mFindTaFragment);
                break;
            case 1:
                if (mRankFragment == null) {
                    mRankFragment = RankFragment.newInstance("排行榜");
                }
                transaction.replace(R.id.layout_frame, mRankFragment);
                break;
            case 2:
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.newInstance("消息");
                }
                transaction.replace(R.id.layout_frame, mMessageFragment);
                break;
            case 3:
                if (mMeFragment == null) {
                    mMeFragment = MeFragment.newInstance("我的");
                }
                transaction.replace(R.id.layout_frame, mMeFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
/*        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
                // ft.commit();
            }
        }*/

    }

    @Override
    public void onTabReselected(int position) {

    }
}
