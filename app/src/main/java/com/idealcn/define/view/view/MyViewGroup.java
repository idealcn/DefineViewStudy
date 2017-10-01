package com.idealcn.define.view.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ideal-gn on 2017/9/11.
 */

public class MyViewGroup extends ViewGroup {
    private ViewDragHelper helper;
    private View mContentView;
    private View mMenuView;
    private int mCurrentTop = 0;
    private DisplayMetrics metrics;
    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //实现ViewDragHelper.Callback相关方法
    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //返回ture则表示可以捕获该view
            return child == mContentView || child == mMenuView;
        }



        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //手指触摸移动时回调, left表示要到的x坐标
            if (child==mMenuView){
                if (left<0)return 0;
                int diffX = metrics.widthPixels - mMenuView.getMeasuredWidth();
                if (left> diffX)
                    return diffX;
            }
            if (child==mContentView){
                if (left<0)return 0;
                int diffX = metrics.widthPixels - mContentView.getMeasuredWidth();
                if (left> diffX)
                    return diffX;
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //手指触摸移动时回调 top表示要到达的y坐标
//            return Math.max(Math.min(top, mMenuView.getHeight()), 0);
            if (child==mContentView) {
                if (dy < 0 && top <= mContentView.getHeight()) return 0;
                if (top >= mContentView.getHeight()) return top;
            }
            if (child==mMenuView) {
                if ( top <0) return 0;
                if (top >=metrics.heightPixels -  mMenuView.getHeight()) return metrics.heightPixels -  mMenuView.getHeight();
            }
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //手指抬起释放时回调

            helper.settleCapturedViewAt(releasedChild.getLeft(), releasedChild.getTop());
            requestLayout();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //mDrawerView完全覆盖屏幕则防止过度绘制
//            changedView.layout(left,top,left+changedView.getWidth(),top+changedView.getHeight());
            helper.smoothSlideViewTo(changedView,left,top);
            Log.d("drag", "onViewPositionChanged: "+left+"-----"+top);
            requestLayout();
        }
        @Override
        public int getViewVerticalDragRange(View child) {
            if (mMenuView == child)
                return metrics.heightPixels - mMenuView.getHeight();
            return
                    (mContentView == child) ?metrics.heightPixels - mContentView.getHeight() : 0;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            if (child==mContentView)
                return metrics.widthPixels - mContentView.getWidth();
            if (child == mMenuView)
                return metrics.widthPixels - mMenuView.getWidth();
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);

        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mContentView = getChildAt(1);
        helper = ViewDragHelper.create(this,1, new ViewDragHelperCallBack());
        helper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT|ViewDragHelper.EDGE_RIGHT);
        metrics = getResources().getDisplayMetrics();
    }


    @Override
    public void computeScroll() {
        if (helper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //onInterceptTouchEvent方法调用ViewDragHelper.shouldInterceptTouchEvent
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = MotionEventCompat.getActionMasked(ev);
        if (actionMasked==MotionEvent.ACTION_CANCEL||actionMasked==MotionEvent.ACTION_UP) {
            helper.cancel();
            return false;
        }
        return helper.shouldInterceptTouchEvent(ev);
    }

    //onTouchEvent方法中调用ViewDragHelper.processTouchEvent方法并返回true
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        helper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec,heightMeasureSpec);



        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuView.layout(0, 0,
                mMenuView.getMeasuredWidth(),
                mMenuView.getMeasuredHeight());
        mContentView.layout(0, mCurrentTop + mMenuView.getHeight(),
                mContentView.getMeasuredWidth(),
                mCurrentTop + mContentView.getMeasuredHeight() + mMenuView.getHeight());

    }


}
