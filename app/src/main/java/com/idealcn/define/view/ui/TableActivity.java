package com.idealcn.define.view.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.idealcn.define.view.R;
import com.idealcn.define.view.databinding.ActivityTableBinding;
import com.idealcn.define.view.listener.OnAddPhotoListener;


/**
 * Created by ideal-gn on 2017/9/15.
 */

public class TableActivity extends AppCompatActivity implements OnAddPhotoListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTableBinding   binding = DataBindingUtil.setContentView(this, R.layout.activity_table);


    }

    @Override
    public Bitmap addByCamera() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
        return bitmap;
    }

    @Override
    public Bitmap addByGallery() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
        return bitmap;
    }
}
