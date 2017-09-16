package com.idealcn.define.view;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * Created by ideal-gn on 2017/9/8.
 */

public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = metrics.densityDpi;
        float scaledDensity = metrics.scaledDensity;
        float density = metrics.density;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        System.out.println();

        Field[] declaredFields = Build.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                System.out.println("field---"+declaredField.getName()+"--value:  "+declaredField.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
