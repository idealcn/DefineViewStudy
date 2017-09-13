package com.idealcn.define.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ideal on 17-8-11.
 */

public class MyView extends View {



    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getW(widthMeasureSpec),
                getH(heightMeasureSpec));
    }

    private int getW(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode==MeasureSpec.EXACTLY){
            return size;
        }
        if (mode==MeasureSpec.AT_MOST){
            int result = 0;
            result = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            return result;
        }
        return 0;
    }

    private int getH(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode==MeasureSpec.EXACTLY){
            return size;
        }
        if (mode==MeasureSpec.AT_MOST){
            int result = 0;
            result = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
            return result;
        }
        return 0;
    }

}
