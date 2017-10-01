package com.idealcn.define.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.idealcn.define.view.R;
import com.idealcn.define.view.recyclerView.adapter.TimeAxisAdapter;
import com.idealcn.define.view.recyclerView.itemdecoration.LeftOffsetDecoration;
import com.idealcn.define.view.recyclerView.itemdecoration.TimeAxisDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ideal-gn
 * date: 2017/9/30.
 */

public class TimeAxisActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_axis);
        List<String> dataList = new ArrayList<>();
        for (int x = 0; x < 30; x++) {
            dataList.add("data---"+x);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new TimeAxisAdapter(this,dataList));
        recyclerView.addItemDecoration(new LeftOffsetDecoration());
        recyclerView.addItemDecoration(new TimeAxisDecoration());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
