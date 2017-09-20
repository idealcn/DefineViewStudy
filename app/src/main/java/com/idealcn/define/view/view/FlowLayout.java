package com.idealcn.define.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealcn.define.view.R;
import com.idealcn.define.view.listener.OnFlowChildClickListener;
import com.idealcn.define.view.utils.DensityUtil;

/**
 * Created by ideal-gn on 2017/9/16.
 * 流式布局
 */

public class FlowLayout extends ViewGroup {

    private Context context;
    private int gravity;
//    int color;
//    int size;
    float scaledDensity;
    private OnFlowChildClickListener listener;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            gravity = typedArray.getInt(R.styleable.FlowLayout_flow_gravity,0);
        } finally {
            typedArray.recycle();
        }

        setBackgroundColor(Color.parseColor("#f4f4f4"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
           FlowLayoutParams lp = (FlowLayoutParams) child.getLayoutParams();
           int top,bottom;
            if (lp.gravity == 1){
//                top =
            }
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
            if (child.getVisibility()==View.GONE)continue;
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            measureChildWithMargins(child,
                    widthMeasureSpec,
                    DensityUtil.dip2px(getContext(),3),//+getPaddingLeft()+getPaddingRight(),
                    heightMeasureSpec,
                    DensityUtil.dip2px(getContext(),3)//+getPaddingTop()+getPaddingBottom()
                    );

            FlowLayoutParams lp = (FlowLayoutParams) child.getLayoutParams();

            if (tempPWidth + childWidth + lp.leftMargin + lp.rightMargin >= wSize){
                lineNumber ++;
                lp.left = lp.leftMargin;
                pWidth = Math.max(tempPWidth,pWidth);
                pHeight += tempPHeight;
                tempPWidth = childWidth +lp.leftMargin + lp.rightMargin;
                tempPHeight = childHeight;
            }else {
                tempPWidth += childWidth + lp.leftMargin + lp.rightMargin;
                tempPHeight = Math.max(childHeight + lp.topMargin + lp.bottomMargin,tempPHeight);
                lp.left = tempPWidth - childWidth -lp.rightMargin;
            }
            lp.top = pHeight;
            if (lineNumber==1){
                lp.top = lp.topMargin;
            }
            if (x==childCount-1){
                pHeight += tempPHeight;
            }
            lp.gravity = gravity;
        }
        if (lineNumber==1){
            pWidth = tempPWidth;
            pHeight = tempPHeight;
        }
        setMeasuredDimension(pWidth,pHeight+ DensityUtil.dip2px(getContext(),5));
    }




    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        context = getContext();
        listener = (OnFlowChildClickListener) getContext();

        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            if (child.getVisibility()!=View.VISIBLE)continue;
            if (listener!=null){
                final int finalX = x;
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onChildClick(finalX);
                    }
                });
            }
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
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
        public int gravity;
        public int maxHeight;//记录某一行某个最大高度

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
