package com.idealcn.define.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealcn.define.view.R;

/**
 * Created by ideal-gn on 2017/9/16.
 * 流式布局
 */

public class FlowLayout extends ViewGroup {

    private Context context;
    int color;
    int size;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);


        color = typedArray.getColor(R.styleable.FlowLayout_textColor, Color.parseColor("#ffffff"));
        size = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_textSize, 20);


        typedArray.recycle();

        setBackgroundColor(Color.parseColor("#f4f4f4"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
           FlowLayoutParams lp = (FlowLayoutParams) child.getLayoutParams();
            child.layout(lp.left,lp.top,lp.left + child.getMeasuredWidth(),lp.top+ child.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);


        int pWidth = 0,pHeight = 0;
        int tempPWidth = 0,tempPHeight = 0;
        int lineNumber = 1;

        int childWidth  = 0,childHeight = 0;

        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            FlowLayoutParams lp = (FlowLayoutParams) child.getLayoutParams();

            if (tempPWidth + childWidth >= wSize){
                lineNumber ++;
                lp.left = 0;
                pWidth = Math.max(tempPWidth,pWidth);
                pHeight += tempPHeight;
                tempPWidth = childWidth;
                tempPHeight = childHeight;
            }else {
                tempPWidth += childWidth + lp.leftMargin + lp.rightMargin;
                tempPHeight = Math.max(childHeight + lp.topMargin + lp.bottomMargin,tempPHeight);
                lp.left = tempPWidth - childWidth ;
            }
            lp.top = pHeight;
            if (x==childCount-1){
                pHeight += tempPHeight;
            }

        }
        if (lineNumber==1){
            pWidth = tempPWidth;
            pHeight = tempPHeight;
        }
        setMeasuredDimension(pWidth,pHeight);
    }




    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        context = getContext();
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof FlowLayoutParams;
    }

    @Override
    protected FlowLayoutParams generateDefaultLayoutParams() {
        return new FlowLayoutParams(FlowLayoutParams.WRAP_CONTENT,
                FlowLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected FlowLayoutParams generateLayoutParams(LayoutParams p) {
        return new FlowLayoutParams(p);
    }

    @Override
    public FlowLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayoutParams(getContext(),attrs);
    }

    //自定义LayoutParams的目的是将子view的left和top在测量的时候就赋值，在onLayout的时候直接取出使用
    public static class FlowLayoutParams extends ViewGroup.MarginLayoutParams{

        public int left,top;

        public FlowLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public FlowLayoutParams(int width, int height) {
            super(width, height);
        }

        public FlowLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
