package com.idealcn.define.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.ActivityClockBinding;
import com.idealcn.define.view.listener.OnTimeChangeListener;

/**
 * Created by ideal-gn on 2017/9/7.
 */

public class ClockActivity extends AppCompatActivity implements OnTimeChangeListener{

    private  ActivityClockBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_clock);
    }

    @Override
    public void change(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.time.setText(time);
            }
        });
    }

    //onResume先于自定义view的onAttachedToWindow()方法执行
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //onDestroy()先于自定义view的onDetachedFromWindow()执行
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
