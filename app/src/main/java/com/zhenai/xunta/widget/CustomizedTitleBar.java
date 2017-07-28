package com.zhenai.xunta.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenai.xunta.R;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public class CustomizedTitleBar extends LinearLayout {
    public CustomizedTitleBar(Context context) {
        super(context);
    }

    public CustomizedTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.titlebar, this);

        ImageButton imageButton = findViewById(R.id.title_image);
        TextView textView = findViewById(R.id.title_text);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizedTitleBar);

        if (typedArray != null){
            int backIconDrawable = typedArray.getResourceId(R.styleable.CustomizedTitleBar_title_back_icon, -1);
            String textString = typedArray.getString(R.styleable.CustomizedTitleBar_title_text);
            boolean isBackIconVisible = typedArray.getBoolean(R.styleable.CustomizedTitleBar_title_back_icon_visible,false); //默认不可见
            if(isBackIconVisible == true){
                imageButton.setVisibility(View.VISIBLE);
            }else {
                imageButton.setVisibility(View.INVISIBLE);
            }

            //设置ImageButton背景
            imageButton.setBackgroundResource(backIconDrawable);

            //点击ImageButton，返回上一页
            imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)getContext()).finish();
                }
            });

            //设置TextView属性
            textView.setText(textString);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(14);
        }

        typedArray.recycle(); //记得回收
    }
}
