package com.idealcn.define.view.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlideDeleteLayout extends ViewGroup {

    private View mContent; // 内容部分
    private View mDelete;  // 删除部分
    private ViewDragHelper helper;
    private int mContentWidth,mContentHeight;
    private int mDeleteWidth;
    private int mDeleteHeight;


    public SlideDeleteLayout(Context context) {
        super(context);
    }

    public SlideDeleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = getChildAt(0);
        mDelete = getChildAt(1);
        helper = ViewDragHelper.create(this, 1, new SimpleCallBack());
    }
    private class SimpleCallBack extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child==mContent || child==mDelete;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            if (capturedChild == mDelete && null!=listener ){
                listener.onDeleteClick(SlideDeleteLayout.this);
            }
            if (capturedChild == mContent && null!=listener){
                listener.onContentClick(SlideDeleteLayout.this);
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            //解决左右滑动越界
            if(child == mContent){ // 解决内容部分左右拖动的越界问题
                if(left>0){//右滑
                    return 0;
                }else if(-left>mDeleteWidth){//左滑
                    return -mDeleteWidth;
                }
            }

            if(child == mDelete){ // 解决删除部分左右拖动的越界问题
                if(left<mContentWidth - mDeleteWidth){
                    return mContentWidth - mDeleteWidth;
                }else if(left > mContentWidth){
                    return mContentWidth;
                }
            }

            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            invalidate();
            if (changedView==mContent){
                int mTempDeleteLeft = mContentWidth + left;
                mDelete.layout(mTempDeleteLeft,top,mTempDeleteLeft+mDeleteWidth,top+mDeleteHeight);
            }
            if (changedView==mDelete){
                mContent.layout(left-mContentWidth,top,left,top+mContentHeight);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int left = mContent.getLeft();
            if (-left>mDeleteWidth/2){
                isShowDelete(true);
                if (listener!=null){
                    listener.onOpen(SlideDeleteLayout.this);
                }
            }else {
                isShowDelete(false);
                if (listener!=null) {
                    listener.onClose(SlideDeleteLayout.this);
                }
            }


            super.onViewReleased(releasedChild, xvel, yvel);
        }
    }

    public void isShowDelete(boolean isShowDelete){
        if (isShowDelete){
            helper.smoothSlideViewTo(mContent,-mDeleteWidth,0);
            helper.smoothSlideViewTo(mDelete,mContentWidth - mDeleteWidth,0);
        }else {

            helper.smoothSlideViewTo(mDelete,mContentWidth,0);
            helper.smoothSlideViewTo(mContent,0,0);
        }
//        invalidate();
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (helper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
//            invalidate();
    }

    /**
     *  todo 如果用作RecyclerView的Adapter的布局,会导致子view显示不正常,而用作Activity的 布局就没问题.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int pWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int pHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureDefineChild(mContent,widthMeasureSpec,heightMeasureSpec,pWidth, pHeight);
        measureDefineChild(mDelete,widthMeasureSpec,heightMeasureSpec,pWidth,pHeight);

        setMeasuredDimension(pWidth
                , Math.max(mContent.getMeasuredHeight(),mDelete.getMeasuredHeight()));
    }

    private void measureDefineChild(View child,int widthMeasureSpec,int heightMeasureSpec,int pWidth, int pHeight) {
        final LayoutParams contentLayoutParams = child.getLayoutParams();
        final int height = contentLayoutParams.height;
        final int width = contentLayoutParams.width;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int contentWidthMeasureSpec = 0;

        if (widthMode == MeasureSpec.EXACTLY){
            if (width== LayoutParams.MATCH_PARENT){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.EXACTLY);
            }else if (width>0){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width,pWidth), MeasureSpec.EXACTLY);
            }else if (width == LayoutParams.WRAP_CONTENT){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width,pWidth), MeasureSpec.AT_MOST);
            }
        }else if (widthMode == MeasureSpec.AT_MOST){
            if (width== LayoutParams.MATCH_PARENT){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.AT_MOST);
            }else if (width>0){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width,pWidth), MeasureSpec.EXACTLY);
            }else if (width == LayoutParams.WRAP_CONTENT){
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width,pWidth), MeasureSpec.AT_MOST);
            }
        }

        int contentHeightMeasureSpec = 0;

        if (heightMode==MeasureSpec.EXACTLY){
            if (height== LayoutParams.MATCH_PARENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY);
            }else if (height== LayoutParams.WRAP_CONTENT){
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight,MeasureSpec.AT_MOST);
            }else if (height>0){
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
        }else if (heightMode==MeasureSpec.AT_MOST){
            if (height== LayoutParams.MATCH_PARENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.AT_MOST);
            }else if (height== LayoutParams.WRAP_CONTENT){
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight,MeasureSpec.AT_MOST);
            }else if (height>0){
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
        }

        child.measure(contentWidthMeasureSpec, contentHeightMeasureSpec );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
         mContentWidth = mContent.getMeasuredWidth();
        mDeleteHeight =   mContentHeight = mContent.getMeasuredHeight();
        mContent.layout(0,0,mContentWidth,mContentHeight); // 摆放内容部分的位置
         mDeleteWidth = mDelete.getMeasuredWidth();
        mDelete.layout(mContentWidth,0,
                mContentWidth + mDeleteWidth, mContentHeight); // 摆放删除部分的位置
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

            helper.processTouchEvent(event);
            return true;

    }

    private OnSlideDeleteListener listener;
    // SlideDlete的接口
    public interface OnSlideDeleteListener {
        void onOpen(SlideDeleteLayout slideDelete);
        void onClose(SlideDeleteLayout slideDelete);

        void onDeleteClick(SlideDeleteLayout slideDelete);

        void onContentClick(SlideDeleteLayout slideDeleteLayout);
    }

    public void setListener(OnSlideDeleteListener listener) {
        this.listener = listener;
    }
}