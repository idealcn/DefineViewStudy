package com.idealcn.define.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealcn.define.view.R;
import com.idealcn.define.view.listener.OnFlowChildClickListener;
import com.idealcn.define.view.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ideal-gn on 2017/9/16.
 * 流式布局
 */

public class FlowLayout extends ViewGroup {

    private Context context;
    private int gravity;

    float scaledDensity;
    private OnFlowChildClickListener listener;

    //记录某一行保存的view
    private SparseArray<List<View>> mLineViewMap = new SparseArray<>();
    private List<View> mViewList  =  new ArrayList<>();

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

        int size = mLineViewMap.size();
        for (int x = 0; x < size; x++) {
            List<View> viewList = mLineViewMap.get(x);
            int maxHeight = 0;
            for (View view : viewList) {
                maxHeight = Math.max(maxHeight,view.getMeasuredHeight());
            }
            for (View view : viewList) {
                FlowLayoutParams lp = (FlowLayoutParams) view.getLayoutParams();
                if (view.getHeight()<maxHeight){
                    view.layout(lp.left,lp.top + (maxHeight - view.getMeasuredHeight())/2,
                            lp.left + view.getMeasuredWidth(),lp.top + (maxHeight + view.getMeasuredHeight())/2);
                }else {
                    view.layout(lp.left,lp.top,lp.left + view.getMeasuredWidth(),lp.top+ view.getMeasuredHeight());
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLineViewMap.clear();
        mViewList.clear();
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);


        int pWidth = 0,pHeight = 0;
        int tempPWidth = 0,tempPHeight = 0;
        int lineNumber = 0;

        int childWidth  = 0,childHeight = 0;

        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {

            View child = getChildAt(x);
            if (child.getVisibility()==View.GONE)continue;

            measureChildWithMargins(child,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                   0
                    );
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();


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

            List<View> viewList = mLineViewMap.get(lineNumber);
            if (viewList==null) {
                viewList = new ArrayList<>();
                mLineViewMap.put(lineNumber,viewList);
            }
            viewList.add(child);
            lp.top = pHeight + lp.topMargin;
            if (lineNumber==0){
                lp.top = lp.topMargin;
            }
            if (x==childCount-1){
                pHeight += tempPHeight;
            }
            lp.gravity = gravity;
        }
        if (lineNumber==0){
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
