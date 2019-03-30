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
 * 标签布局容器
 */

public class FlowLayout extends ViewGroup {

    private int gravity;

    private float scaledDensity;
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
            //该行的最大高度
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
        //父容器为FlowLayout指定的宽度
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        //父容器为FlowLayout指定的高度
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        //该容器允许的最大宽度和高度
        int pWidth = 0,pHeight = 0;
        //每次测量后,当前行的累加后的宽和当前行最大高度
        int tempPWidth = 0,tempPHeight = 0;
        //子view处于第几行
        int lineNumber = 0;
        //某个子view的宽和高
        int childWidth  = 0,childHeight = 0;
        //子view个数
        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {

            View child = getChildAt(x);
            if (child.getVisibility()==View.GONE){
                continue;
            }
            //测量子view,并且考虑了该子view的padding和margin
            measureChildWithMargins(child,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                   0
                    );
            //得到该子view的宽和高
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();


            FlowLayoutParams lp = (FlowLayoutParams) child.getLayoutParams();

            if (tempPWidth + childWidth + lp.leftMargin + lp.rightMargin >= wSize){
                lineNumber ++;
                lp.left = lp.leftMargin;
                //换行后,求出该容器的最大宽度
                pWidth = Math.max(tempPWidth,pWidth);
                //换行后,累加该容器的高度
                pHeight += tempPHeight;
                tempPWidth = childWidth +lp.leftMargin + lp.rightMargin;
                tempPHeight = childHeight;
            }else {
                //当前行累加后的宽度
                tempPWidth += (childWidth + lp.leftMargin + lp.rightMargin);
                //当前行最大高度
                tempPHeight = Math.max(childHeight + lp.topMargin + lp.bottomMargin,tempPHeight);
                //子view距离该容器左边界的距离
                lp.left = tempPWidth - childWidth -lp.rightMargin;
            }
            //子view距离父容器上边界的距离
            lp.top = pHeight + lp.topMargin;

            if (x==childCount-1){
                //遍历到最后一个子view,累加该容器高度
                pHeight += tempPHeight;
                //遍历到最后一个子view,求出该容器的最大宽度
                pWidth = Math.max(tempPWidth, pWidth);
            }
            lp.gravity = gravity;


            //添加该子view
            List<View> viewList = mLineViewMap.get(lineNumber);
            if (viewList==null) {
                viewList = new ArrayList<>();
                mLineViewMap.put(lineNumber,viewList);
            }
            viewList.add(child);
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

        int left,top;
        int gravity;
        public int maxHeight;//记录某一行某个最大高度

        FlowLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        FlowLayoutParams(int width, int height) {
            super(width, height);
        }

        FlowLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
