package com.zhenai.xunta.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 查看图片大图的ViewPager适配器 ；已经废弃
 * Created by wenjing.tang on 2017/7/31.
 */

public class PhotoViewPagerAdapter extends PagerAdapter {

    List<Integer> imgList;

    Context mContext;

    public PhotoViewPagerAdapter(Context context, List<Integer> imgs) {
        this.mContext = context;
        this.imgList = imgs;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 初始化Item，用Glide加载图片
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(mContext);
        Glide.with(mContext)
                .load(imgList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        container.addView(photoView);

        return photoView;
    }
}
