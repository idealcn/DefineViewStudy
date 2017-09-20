package com.idealcn.define.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.ActivityFlowBinding;
import com.idealcn.define.view.listener.OnFlowChildClickListener;

/**
 * Created by ideal-gn on 2017/9/16.
 */

public class FlowLayoutActivity extends AppCompatActivity implements OnFlowChildClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      ActivityFlowBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_flow);

    }

    @Override
    public void onChildClick(int index) {
        Toast.makeText(FlowLayoutActivity.this, "-------"+index, Toast.LENGTH_SHORT).show();
    }
}
