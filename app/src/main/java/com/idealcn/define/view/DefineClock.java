package com.idealcn.define.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ideal on 17-8-11.
 */

public class DefineClock extends View {

    private Paint mOuterCirclePaint;
    private Paint mTextPaint;

    private int CENTER_X,CENTER_Y;
    private int RADIUS;
    //偏移量，避免圆超过边界
    private final int OFFSET = 10;


    public DefineClock(Context context) {
        this(context,null);
    }

    public DefineClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefineClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setColor(Color.parseColor("#345678"));
        mOuterCirclePaint.setStrokeWidth(3);
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#345678"));
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(50);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CENTER_X = getWidth()/2;
        CENTER_Y = getHeight()/2;
        RADIUS = Math.min(getWidth(),getHeight())/2 - OFFSET;



        canvas.drawCircle(CENTER_X,CENTER_Y,RADIUS,mOuterCirclePaint);
//        canvas.drawText(String.valueOf(12),CENTER_X,40,mTextPaint);
        Rect rect = null;
        String text = null;
        int textWidth,textHeight;
        for (int i = 12; i >0; i--) {
            canvas.save();

            rect = new Rect();
            text = String.valueOf(i);
            mTextPaint.getTextBounds(text,0,text.length(),rect);
            textWidth = rect.right - rect.left;
            textHeight = rect.bottom - rect.top;

            canvas.drawText(String.valueOf(i),
                    CENTER_X - mTextPaint.measureText(text)/2,
                   OFFSET+textHeight+mTextPaint.getStrokeWidth()+5,
                    mTextPaint);
            canvas.rotate(30);
            canvas.restore();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getClockWidth(widthMeasureSpec)
        ,getClockHeight(heightMeasureSpec));

    }

    private int getClockHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        if (mode==MeasureSpec.EXACTLY){
            result = getMeasuredHeight();
        }else if (mode==MeasureSpec.AT_MOST){
            result = size;
        }
        return result;
    }

    private int getClockWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        if (mode==MeasureSpec.EXACTLY){
            result = getMeasuredWidth();
        }else if (mode==MeasureSpec.AT_MOST){
            result = size;
        }
        return result;
    }
}
