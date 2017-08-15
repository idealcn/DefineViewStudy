package com.idealcn.define.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ideal on 17-8-15.
 */

public class MyViewGroup extends ViewGroup {


    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int mLeft,mTop,mRight,mBottom;

        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            mLeft = (right - child.getMeasuredWidth())/2;
            mTop = (bottom - child.getMeasuredHeight())/2;
            child.layout(mLeft,mTop,right - mLeft,bottom - mTop);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();

        int resultWidth;
        int resultHeight;

        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,0);

        }
    }
}
