package com.zhenai.xunta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhenai.xunta.R;

import java.util.List;

/**
 * 水平滚动RecyclerView的Adapter
 * Created by wenjing.tang on 2017/7/25.
 */

public class PictureGallaryAdapter extends RecyclerView.Adapter<PictureGallaryAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    private Context context;

    public static int index;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public PictureGallaryAdapter(Context context, List<Integer> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.context = context;
    }

    //创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.picture_recycle_view_item, parent, false); //将item转换为布局
        final ViewHolder viewHolder = new ViewHolder(view); //创建ViewHodler
        viewHolder.imageView = view.findViewById(R.id.iv_recycleview_item);//设置ViewHolder的属性

        return viewHolder;
    }

    //将数据（图片）设置到创建的ViewHolder实例
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
       // holder.imageView.setImageResource(mDatas.get(position));

        Glide.with(context) //使用Glide加载图片
                .load(mDatas.get(position))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v){
                    int position = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, position);

                    index = position;
                }
            });
        }

    }

     public void addData(int position)  {
        // mDatas.add(position, 0);
          //notifyItemInserted(position);
      }


    public void removeData(int position) {
       //mDatas.remove(position);//List或ArrayList才有添加移除方法
       // notifyItemRemoved(position);
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
