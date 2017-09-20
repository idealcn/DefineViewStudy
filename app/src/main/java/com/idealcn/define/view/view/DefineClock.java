package com.idealcn.define.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.idealcn.define.view.listener.OnTimeChangeListener;
import com.idealcn.define.view.utils.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ideal on 17-8-11.
 */

public class DefineClock extends View {

    private Paint mOuterCirclePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;

    private int CENTER_X,CENTER_Y;
    private int RADIUS;
    //偏移量，避免圆超过边界
    private  int OFFSET = 10;

    private   Rect rect = new Rect();

    private OnTimeChangeListener listener;

    private Timer timer;

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
        mOuterCirclePaint.setStrokeWidth(DensityUtil.dip2px(context,3));
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#125678"));
        mTextPaint.setStrokeWidth(DensityUtil.dip2px(context,3));
        mTextPaint.setTextSize(DensityUtil.dip2px(context,15));
        mTextPaint.setStyle(Paint.Style.FILL);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.RED);
        mPointPaint.setStrokeWidth(DensityUtil.dip2px(getContext(),5));


        OFFSET = DensityUtil.dip2px(context,OFFSET);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        listener = (OnTimeChangeListener) getContext();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    long l = System.currentTimeMillis();
                    Date date = new Date(l);
                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd kk:mm:ss", Locale.SIMPLIFIED_CHINESE);
                    String time = format.format(date);
                    listener.change(time);
            }
        },0,100);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
        if (listener!=null)
            listener = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//      这里的圆心坐标是相对于view自身取值的.
        CENTER_X = CENTER_Y = Math.min(getWidth(),getHeight())/2;
        RADIUS = Math.min(getWidth()-getPaddingLeft()-getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom())/2 - OFFSET;



        canvas.drawCircle(CENTER_X,CENTER_Y,RADIUS,mOuterCirclePaint);

        canvas.drawPoint(CENTER_X,CENTER_Y,mPointPaint);

        String text = null;
        int textHeight;
        canvas.save();
        for (int i = 12; i >0; i--) {
            //保存画布当前的状态
            //canvas.save();
            //以圆心为轴心旋转画布
//            canvas.rotate(30*(-12+i),CENTER_X,CENTER_Y);


            rect.setEmpty();
            text = String.valueOf(i);
            mTextPaint.getTextBounds(text,0,text.length(), rect);
            textHeight = rect.bottom - rect.top;
            canvas.drawText(String.valueOf(i),
                    CENTER_X - mTextPaint.measureText(text)/2 ,
                   OFFSET+textHeight+mTextPaint.getStrokeWidth()+5 + getPaddingTop(),
                    mTextPaint);
            canvas.rotate(-30,CENTER_X,CENTER_Y);
            //取出画布保存的状态,这一步如果放到for循环之后,那么画布每次旋转的角度都是30赌
//            canvas.restore();
        }
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("clock", "onMeasure: "+getPaddingLeft()+"---"+getPaddingRight());

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
