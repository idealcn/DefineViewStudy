package com.idealcn.define.view.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.idealcn.define.view.utils.LoggerUtil;

/**
 * Created by ideal-gn on 2017/9/11.
 */

public class SlideLayout extends ViewGroup {

    private         LoggerUtil logger;


    private ViewDragHelper helper;
    private View mContentView;
    private View mMenuView;
    private int mCurrentTop = 0;
    private DisplayMetrics metrics;
    public SlideLayout(Context context) {
        this(context,null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        logger = LoggerUtil.INSTANCE.logger;

    }

    //实现ViewDragHelper.Callback相关方法
    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == mContentView || child == mMenuView;
        }



        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            //手指触摸移动时回调, left表示要到的x坐标
            int mContentViewWidth = mContentView.getWidth();
            int mMenuViewWidth = mMenuView.getWidth();
            if (child == mMenuView) {
               int leftMenuEdge = mContentViewWidth - mMenuViewWidth;
               if (left < leftMenuEdge) return leftMenuEdge;
               if (left > mContentViewWidth) return mContentViewWidth;
           }
           if (child == mContentView ){
               int leftContentEdge = -mMenuViewWidth;
               if (left < leftContentEdge) return leftContentEdge;
               if (left > 0) return 0;
           }
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            logger.info("onViewReleased-------------");
            float mContentViewX = mContentView.getX();
            float mMenuViewX = mMenuView.getX();
            int mMenuWidth = mMenuView.getWidth();
            int mContentWidth = mContentView.getWidth();
            logger.info("context: x: "+ mContentViewX);
            logger.info("menu: x: "+ mMenuViewX);
            logger.info("ViewGroup: x: "+getX());
            logger.info("-------------onViewReleased");
            //手指抬起释放时回调
            if (releasedChild == mContentView){
                if (mMenuWidth /2 > Math.abs(mContentViewX)){
                    helper.smoothSlideViewTo(mMenuView, mContentWidth,0);
                    helper.smoothSlideViewTo(mContentView,0, 0);
                }else {
                    helper.smoothSlideViewTo(mMenuView, (int) (mContentWidth - mMenuWidth),0);
                    helper.smoothSlideViewTo(mContentView,-mMenuWidth, 0);
                }
            }

            if (releasedChild == mMenuView){
                if (mContentWidth - mMenuViewX < mMenuWidth/2){
                    helper.smoothSlideViewTo(mMenuView,mContentWidth,0);
                    helper.smoothSlideViewTo(mContentView,0,0);
                }else {
                    helper.smoothSlideViewTo(mMenuView, (int) (mContentWidth - mMenuWidth),0);
                    helper.smoothSlideViewTo(mContentView,-mMenuWidth, 0);
                }
            }
            invalidate();
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {

            logger.info("onViewPositionChanged-------------");
            logger.info("context: x: "+mContentView.getX());
            logger.info("menu: x: "+mMenuView.getX());
            logger.info("ViewGroup: x: "+getX());
            logger.info("-------------onViewPositionChanged");

            if (changedView==mContentView){
                float x = mContentView.getX();
                mContentView.layout((int) x,0, (int) (mContentView.getWidth() + x),getHeight());
                mMenuView.layout((int) (mContentView.getWidth() + x),0, (int) (mContentView.getWidth() + x + mMenuView.getWidth()),getHeight());
                mContentView.offsetTopAndBottom(0);

            }

            if (changedView == mMenuView){
                float x = mMenuView.getX();
                mMenuView.layout((int) x,0, (int) (x+mMenuView.getWidth()),getHeight());
                mContentView.layout((int)(x - mContentView.getWidth()),0, (int) x,getHeight());
            }
          invalidate();
        }
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
           return 0;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return  mMenuView.getWidth();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(1);
        mContentView = getChildAt(0);
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



        SlideLayoutParams layoutParams = (SlideLayoutParams) mMenuView.getLayoutParams();
        layoutParams.leftEdge = mContentView.getMeasuredWidth();
        layoutParams.topEdge = 0;

//        layoutParams = (SlideLayoutParams) mContentView.getLayoutParams();
//        layoutParams.leftEdge = 0;
//        layoutParams.topEdge = 0;

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width,height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuView.layout(
                (int) ((SlideLayoutParams) mMenuView.getLayoutParams()).leftEdge,
                0,
                getMeasuredWidth() + mMenuView.getMeasuredWidth(),
                getMeasuredHeight()
        );
        mContentView.layout( (int) ((SlideLayoutParams) mContentView.getLayoutParams()).leftEdge,
                0,
                getMeasuredWidth(),
                getMeasuredHeight()
        );

    }


    @Override
    protected SlideLayoutParams generateDefaultLayoutParams() {
        return new SlideLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected SlideLayoutParams generateLayoutParams(LayoutParams p) {
        return new SlideLayoutParams(p);
    }

    @Override
    public SlideLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new  SlideLayoutParams(getContext(),attrs);
    }

    public static class SlideLayoutParams extends ViewGroup.LayoutParams{

        public float leftEdge;
        public float topEdge;

        public SlideLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public SlideLayoutParams(int width, int height) {
            super(width, height);
        }

        public SlideLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
