package com.idealcn.define.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.idealcn.define.view.view.ColorfulCircle;
import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.ActivityProgressbarBinding;

/**
 * Created by ideal-gn on 2017/9/7.
 */

public class ProgressBarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "define";
    private ActivityProgressbarBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_progressbar);
        binding.setClick(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.line:
                binding.colorCircle.setProgressType(ColorfulCircle.LINE);
                break;
            case R.id.circle:
                binding.colorCircle.setProgressType(ColorfulCircle.CIRCLE);
                break;
            default:break;
        }
    }
}
