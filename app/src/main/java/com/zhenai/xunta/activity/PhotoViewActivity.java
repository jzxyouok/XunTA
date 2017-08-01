package com.zhenai.xunta.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.zhenai.xunta.R;
import com.zhenai.xunta.adapter.PhotoViewPagerAdapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

import static com.zhenai.xunta.adapter.PictureGallaryAdapter.index;

/**
 * Created by wenjing.tang on 2017/7/31.
 */

public class PhotoViewActivity extends BaseActivity {

    private PhotoView mPhotoView;
    private ViewPager mImageViewPager;
    private TextView mTvImageCount;

    private List<Integer> imgList;
    private  int currentPosition ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        mImageViewPager = (ViewPager) findViewById(R.id.img_viewpager);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);

        imgList = getIntent().getIntegerArrayListExtra("image_data");
        currentPosition = getIntent().getIntExtra("index",0);

        mTvImageCount.setText(currentPosition+1 + "/" + imgList.size()); //设置默认显示1/9

        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(this, imgList);
        mImageViewPager.setAdapter(adapter);
        mImageViewPager.setCurrentItem(currentPosition);

        mImageViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                index = position;
                mTvImageCount.setText(index + 1 + "/" + imgList.size());
            }
        });

        /*mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                Log.e("onPhotoTap","onPhotoTap");
               // finish();
                onBackPressed();
            }
        });*/

    }
}
