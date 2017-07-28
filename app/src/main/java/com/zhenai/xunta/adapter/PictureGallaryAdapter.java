package com.zhenai.xunta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhenai.xunta.R;

import java.util.List;

/**
 * Created by wenjing.tang on 2017/7/25.
 */

public class PictureGallaryAdapter extends RecyclerView.Adapter<PictureGallaryAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public PictureGallaryAdapter(Context context, List<Integer> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    //创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.picture_recycle_view_item, parent, false); //将item转换为布局
        ViewHolder viewHolder = new ViewHolder(view); //创建ViewHodler
        viewHolder.imageView = view.findViewById(R.id.iv_recycleview_item);//设置ViewHolder的属性
        return viewHolder;
    }

    //将数据（图片）设置到创建的ViewHolder实例
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView imageView;
    }
}
