package com.zhenai.xunta.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenai.xunta.R;

/**
 * “设置”的页面中的布局控件，实现布局复用
 * Created by wenjing.tang on 2017/7/29.
 */

public class ItemSettingLayout extends LinearLayout {

    public ItemSettingLayout(Context context) {
        super(context);
    }

    public ItemSettingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_setting_item, this);

        TextView text = findViewById(R.id.tv_text); //item对应的文字

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemSettingLayout);

        if (typedArray != null){

            String textString = typedArray.getString(R.styleable.ItemSettingLayout_text);
            text.setText(textString);
            text.setTextSize(14);
        }

    }
}
