package com.idealcn.define.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.idealcn.define.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿照手机桌面宫格布局
 */
public class HorizontalGridView extends ViewGroup {

    /* 每列放置数目*/
    private int columnCount = 4;
    /* 每页最多多少行 */
    private int maxRow = 5;
    /* 每页可放置20个 */
    private int pagerNumber = 20;
    /* 当前第几页 */
    private int currentPage = 0;
    /* 页数 */
    private int pageCount = 1;

    private GestureDetector gestureDetector;


    private List<View> childViewList = new ArrayList<>();

    public HorizontalGridView(Context context) {
        super(context);
    }

    public HorizontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }


        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = childViewList.size();
        final int i = childCount % pagerNumber;
        if (i>0) {
            pageCount = childCount / pagerNumber + 1;
        }else {
            pageCount = childCount / pagerNumber;
        }
        int remain = 0;
        for (int x = 0; x < pageCount; x++) {
             List<View> subList;
            if (x==pageCount-1){
                remain = childCount - x*pagerNumber;
                subList = childViewList.subList( x * pagerNumber,x * pagerNumber+remain);
            }else {
                subList = childViewList.subList( x * pagerNumber,(x+1)*pagerNumber);
            }
            final int size = subList.size();
            for (int y = 0; y < size; y++) {
                int leftDistance = 0;
                int topDistance = (y%columnCount) *(   childViewList.get(y).getMeasuredHeight());
                if (y%columnCount!=0){
                    final View lastChild = subList.get(y-1);
                    leftDistance += lastChild.getMeasuredWidth();
                }
                final View subChild = subList.get(y);
                subChild.layout(x*getWidth()+leftDistance,topDistance,
                        x*getWidth()+leftDistance+ subChild.getMeasuredWidth(),topDistance + 100/*subChild.getHeight()*/);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageView imageView ;
        for (int x = 0; x < 123; x++) {
            imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.ic_launcher_round);
             LayoutParams layoutParams = imageView.getLayoutParams();
            if (null==layoutParams){
                layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
            }
            childViewList.add(imageView);
            addView(imageView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);

    }
}
