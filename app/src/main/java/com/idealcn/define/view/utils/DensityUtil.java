package com.idealcn.define.view.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_IN;
import static android.util.TypedValue.COMPLEX_UNIT_MM;
import static android.util.TypedValue.COMPLEX_UNIT_PT;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by ideal-gn on 2017/9/7.
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale  + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final int scale = (int) context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);


    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics)
    {
        switch (unit) {

            case COMPLEX_UNIT_PX:

                return value;

            case COMPLEX_UNIT_DIP:

                return value * metrics.density;

            case COMPLEX_UNIT_SP:

                return value * metrics.scaledDensity;

            case COMPLEX_UNIT_PT:

                return value * metrics.xdpi * (1.0f / 72);

            case COMPLEX_UNIT_IN:

                return value * metrics.xdpi;

            case COMPLEX_UNIT_MM:

                return value * metrics.xdpi * (1.0f / 25.4f);

        }

        return 0;

    }
}
