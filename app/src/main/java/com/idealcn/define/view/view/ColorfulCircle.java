package com.idealcn.define.view.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.idealcn.define.view.R;
import com.idealcn.define.view.utils.DensityUtil;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by ideal-gn on 2017/8/22.
 */

public class ColorfulCircle extends View {

    private  RectF rectF = new RectF();

    private int progressType;
    public static final int CIRCLE = 0;
    public static final int LINE = 1;

    private float progress = 0.01f;


    private ObjectAnimator lastAnimator;

    private int strokeWidth ;
    private Paint mTextPaint;
    private Paint mCirclePaint;
//    private float CENTER_X,CENTER_Y;
//    private float RADIUS;
//    private int[] colorArray =new int[]{android.R.color.black,android.R.color.holo_red_dark,
//    android.R.color.holo_green_dark};
    public ColorfulCircle(Context context) {
        this(context,null);
    }

    public ColorfulCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorfulCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfulCircle);

        progressType = typedArray.getInt(R.styleable.ColorfulCircle_progress_type, CIRCLE);
        typedArray.recycle();


        //适配画笔宽度
        strokeWidth  = DensityUtil.dip2px(getContext(),5);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStrokeWidth(strokeWidth);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setColor(getResources().getColor(R.color.colorAccent));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
        mTextPaint.setStrokeWidth(strokeWidth);
        mTextPaint.setStyle(Paint.Style.FILL);
        //适配字体
        float textSize = DensityUtil.applyDimension(COMPLEX_UNIT_SP, 20f, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(textSize+0.5f);



    }

    /**
     * 自定义的属性,供ObjectAnimator调用
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width  = getWidth();
        int height = getHeight();
        String text = (int)(100*progress)+"%";
        canvas.drawText(text,
               width/2 - mTextPaint.measureText(text)/2,
                height/2,
                mTextPaint);

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        //设置矩形的区域应当是view的实际大小去掉padding，一定要把padding考虑进来,避免xml布局中设置了padding
        int defaultLeftMargin = DensityUtil.dip2px(getContext(),15);
        rectF.set(strokeWidth+ defaultLeftMargin + getPaddingLeft(),
                strokeWidth+ defaultLeftMargin + getPaddingTop(),
                getWidth() - 2*(strokeWidth+ defaultLeftMargin)-getPaddingLeft() - getPaddingRight(),
                getHeight()-2*(strokeWidth+ defaultLeftMargin)- getPaddingTop() - getPaddingBottom()) ;

        switch (progressType){
            case CIRCLE:
                canvas.drawArc(rectF, 0,  (360*progress + 0.5f),false,mCirclePaint);
                break;
            case LINE:
                canvas.drawLine(getPaddingLeft() ,
                        getPaddingTop()+ getHeight()/2,
                        getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight())*progress,
                        getPaddingTop()+ getHeight()/2,
                        mCirclePaint);
                break;
                default:
                    canvas.drawArc(rectF,0, (float) (2*Math.PI*progress + 0.5f),false,mCirclePaint);
                    break;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width,height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        }else {
            width = widthSize;
        }


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode==MeasureSpec.EXACTLY){
            height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        }else {
            height = heightSize;
        }

        setMeasuredDimension(width,height);
    }

    /**
     * 看官方文档
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 看官方文档注释
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        initAnimator();

    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //页面销毁时,释放动画
        if (lastAnimator!=null) {
            //动画中止,移除所有动画监听
            lastAnimator.removeAllListeners();
            lastAnimator.cancel();
            lastAnimator = null;
        }
    }


    public void setProgressType(int type) {
        this.progressType = type;

        if (null!=lastAnimator){
            cancelAnimator();
        }
         initAnimator();
    }

    private void initAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0.01f,0.3f,0.5f,0.75f);
        lastAnimator = animator;
        animator.setDuration(10000);
        animator.start();
    }


    private void cancelAnimator(){

        if (null!=lastAnimator){
            lastAnimator.cancel();
            lastAnimator = null;
        }


    }
}
