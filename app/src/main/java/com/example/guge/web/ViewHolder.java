package com.example.guge.web;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GUGE on 2017/10/21.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public ViewHolder(Context context, View itemView, ViewGroup parent){
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static ViewHolder get(Context context,ViewGroup parent,int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder holder = new ViewHolder(context,itemView,parent);
        return holder;
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}
