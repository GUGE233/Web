package com.example.guge.web;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by GUGE on 2017/12/24.
 */

public abstract class RecAdapter2 extends RecyclerView.Adapter<ViewHolder> {
    private Context mcontext;
    private int mlayoutId;
    private List<repos> mdata;
    public RecAdapter.OnItemClickListener mOnItemClickListener;

    public RecAdapter2(Context context,int layoutId,List<repos> data){
        mcontext = context;
        mlayoutId = layoutId;
        mdata = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        ViewHolder viewHolder= ViewHolder.get(mcontext,parent,mlayoutId);
        return viewHolder;
    }


    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }


    public void setOnItemClickListener(RecAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final  ViewHolder holder,int position){
        convert(holder,mdata.get(position));
        //设置点击监听器
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }

    }

    public abstract void convert(ViewHolder holder, repos item);

    @Override
    public int getItemCount(){
        return mdata.size();
    }



}
