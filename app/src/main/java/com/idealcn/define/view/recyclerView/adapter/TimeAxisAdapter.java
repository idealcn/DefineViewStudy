package com.idealcn.define.view.recyclerView.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealcn.define.view.recyclerView.holder.TimeAxisHolder;

import java.util.List;

/**
 * author: ideal-gn
 * date: 2017/9/30.
 */

public class TimeAxisAdapter extends RecyclerView.Adapter<TimeAxisHolder> {
    private List<String> dataList;
    private LayoutInflater inflater;
    public TimeAxisAdapter(Context context,List<String> dataList){
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TimeAxisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
        return new TimeAxisHolder(content);
    }

    @Override
    public void onBindViewHolder(TimeAxisHolder holder, int position) {
        holder.mTextView.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
