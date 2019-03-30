package com.idealcn.define.view.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GestureDragView extends View {

    private GestureDetectorCompat gestureDetectorCompat;
    private ScaleGestureDetectorCompat scaleGestureDetectorCompat;

    public GestureDragView(Context context) {
        this(context,null);
    }

    public GestureDragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureDragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground(new ColorDrawable(Color.MAGENTA));
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
               ViewGroup parent = (ViewGroup) getParent();
                parent.scrollBy((int) distanceX,(int) distanceY);
                System.out.println("distanceX: "+distanceX+",distanceY: "+distanceY);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }


            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
