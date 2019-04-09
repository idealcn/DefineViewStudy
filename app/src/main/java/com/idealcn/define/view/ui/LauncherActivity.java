package com.idealcn.define.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idealcn.define.view.R;
import com.idealcn.define.view.view.HorizontalRecyclerView;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        HorizontalRecyclerView recyclerView =  findViewById(R.id.horizontalRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        final BaseQuickAdapter<String,BaseViewHolder> baseQuickAdapter =
                new BaseQuickAdapter<String,BaseViewHolder>(R.layout.adapter_horizontal) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
            }
        };
        final ArrayList<String> list = new ArrayList<>();
        for (int x = 0; x < 123; x++) {
            list.add("item-"+x);
        }
        baseQuickAdapter.addData(list);
        baseQuickAdapter.bindToRecyclerView(recyclerView);
    }
}
