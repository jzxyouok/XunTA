package com.zhenai.xunta.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenai.xunta.R;

/**
 * “我”的页面中的布局控件，实现布局复用
 * Created by wenjing.tang on 2017/7/25.
 */

public class ItemLinearLayout extends LinearLayout{

/*    private LinearLayout ll_click;
    private ImageButton imgBtn_click;*/
    public ItemLinearLayout(Context context) {
        super(context);
    }

    public ItemLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.fragment_me_item, this);

/*        ll_click = findViewById(R.id.ll_click);
        imgBtn_click = findViewById(R.id.imgBtn_click);*/

        ImageView icon = findViewById(R.id.imgView_icon); //item图标
        TextView text = findViewById(R.id.tv_text); //item图标对应的文字

        /*icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onItemClick();
                }
            }
        });*/

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemLinearLayout);

        if (typedArray != null){
            int iconDrawable = typedArray.getResourceId(R.styleable.ItemLinearLayout_icon_drawable, -1);
            String textString = typedArray.getString(R.styleable.ItemLinearLayout_icon_text);
            icon.setBackgroundResource(iconDrawable);
            text.setText(textString);
            text.setTextSize(14);
        }

        typedArray.recycle();

    }

    public void addOnItemClickListener(OnItemLinearLayoutListener listener){
        mListener=listener;
    }

    private OnItemLinearLayoutListener mListener;

    public interface OnItemLinearLayoutListener{
        void onItemClick();
    }
}
