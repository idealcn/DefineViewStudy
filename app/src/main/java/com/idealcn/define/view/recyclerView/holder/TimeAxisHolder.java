package com.idealcn.define.view.recyclerView.holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * author: ideal-gn
 * date: 2017/9/30.
 */

public class TimeAxisHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public TimeAxisHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        mTextView.setTextColor(Color.BLACK);
    }
}
