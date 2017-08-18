package com.idealcn.define.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ideal on 17-8-11.
 */

public class MyView extends View {

    private static final String TAG = "MyView";
    private DisplayMetrics metrics;

//    private int myViewHeight,myViewWidth;
    private String myViewText;

    private Paint mTextPaint;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);

//        myViewWidth = (int) typedArray.getDimension(R.styleable.MyView_myView_width,100);
//        myViewHeight = (int) typedArray.getDimension(R.styleable.MyView_myView_height,100);
        myViewText = (String) typedArray.getText(R.styleable.MyView_myView_text);
        if (TextUtils.isEmpty(myViewText))
            myViewText = "未知数据";





        typedArray.recycle();
        metrics = getResources().getDisplayMetrics();
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(20);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = getRect();
        canvas.drawText(myViewText,
                0,//mTextPaint.measureText(myViewText))/2,
                (getHeight()+(rect.bottom - rect.top))/2,
                mTextPaint);
    }

    @NonNull
    private Rect getRect() {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(myViewText,0,myViewText.length(),rect);
        return rect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(myViewWidth,myViewHeight);
        setMeasuredDimension(getWidth(widthMeasureSpec),getHeight(heightMeasureSpec));
    }

    private int getHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "getHeight: "+mode);
//        if (myViewHeight>size) {
            if (mode == MeasureSpec.EXACTLY) {
                result = getMeasuredHeight();
            } else {
                if (mode == MeasureSpec.AT_MOST) {
//                    Rect rect = getRect();
//                    result = rect.bottom - rect.top;
                    Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
                    result = (int) (metrics.descent - metrics.ascent +metrics.bottom);
//                    Log.d(TAG, "getHeight: "+height);

                }
            }
//        }else {
//            result = myViewHeight;
//        }
        return result;
    }



    private int getWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        Log.d(TAG, "getWidth: "+mode);
//        if (myViewWidth>size) {
            switch (mode) {
                case MeasureSpec.AT_MOST:

                    result = (int) mTextPaint.measureText(myViewText);
                    break;
                case MeasureSpec.EXACTLY:
                    result = getMeasuredWidth();
                    break;
            }
//        }else {
//            result = myViewWidth;
//        }
        return result;
    }

    public String getMyViewText() {
        return myViewText;
    }

    public void setMyViewText(String myViewText) {
        this.myViewText = myViewText;
        requestLayout();
//        invalidate();
    }
}
