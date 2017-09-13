package com.idealcn.define.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.idealcn.define.view.R;

/**
 * Created by ideal-gn on 2017/9/7.
 */

public class RoundCake extends View {

    private RectF rect = new RectF();

    private Paint paint;

    public RoundCake(Context context) {
        this(context,null);
    }

    public RoundCake(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundCake(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingLeft() -getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());

        paint.setColor(getResources().getColor(R.color.colorAccent));
        //sweepAngle表示旋转的度数
        canvas.drawArc(rect,0,130f,true,paint);

        rect.set(0,
                0,
                getWidth(),
                getHeight() );

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawArc(rect,131f,80f,true,paint);
//
//
        rect.set(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingLeft() -getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());
        paint.setColor(Color.CYAN);
        canvas.drawArc(rect,212f,50f,true,paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getCakeWidth(widthMeasureSpec),
                getCakeHeight(heightMeasureSpec));
    }

    private int getCakeHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY){
            result = getMeasuredHeight();
        }
        if (mode == MeasureSpec.AT_MOST){
            result = size;
        }
        return result;
    }

    private int getCakeWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY){
            result = getMeasuredWidth();
        }
        if (mode == MeasureSpec.AT_MOST){
            result = size;
        }
        return result;
    }
}
