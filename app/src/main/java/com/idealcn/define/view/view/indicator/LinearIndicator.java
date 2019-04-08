package com.idealcn.define.view.view.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;


import com.idealcn.define.view.R;

import java.util.ArrayList;
import java.util.List;

public class LinearIndicator extends View {

    //是否可以水平滚动
    public static final int SCROLL_MODE_SCROLLABLE = 0;
    public static final int SCROLL_MODE_FIXED = 1;
    private int mScrollModel = SCROLL_MODE_FIXED;

    //下方的指示线条
    private Paint mIndicatorLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //指示线条的高度
    private int mIndicatorLineHeight = 10;
    //指示线条的宽度
    private int mIndicatorLineWidth = 46;
    //指示线条的颜色
    private int mIndicatorLineColor = Color.BLUE;


    private Paint mIndicatorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mDefaultTextColor = Color.BLACK;

    private int mSelectedTextColor = Color.BLUE;

    private int mTextSize = 13;

    private List<String> titleArray = new ArrayList<>();


    private         int scaledTouchSlop;

    public LinearIndicator(Context context) {
        this(context, null);
    }

    public LinearIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearIndicator);
            mIndicatorLineWidth = typedArray.getDimensionPixelSize(R.styleable.LinearIndicator_indicator_line_width, 60);
            mIndicatorLineHeight = typedArray.getDimensionPixelSize(R.styleable.LinearIndicator_indicator_line_height, 10);
            mIndicatorLineColor = typedArray.getColor(R.styleable.LinearIndicator_indicator_line_color, Color.BLUE);
            mDefaultTextColor = typedArray.getColor(R.styleable.LinearIndicator_indicator_text_color, Color.BLUE);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.LinearIndicator_indicator_text_size, 16);
            mScrollModel = typedArray.getInteger(R.styleable.LinearIndicator_indicator_scroll_mode, SCROLL_MODE_SCROLLABLE);
            CharSequence[] textArray = typedArray.getTextArray(R.styleable.LinearIndicator_indicator_text);
            for (CharSequence sequence : textArray) {
                titleArray.add(sequence.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != typedArray) {
                typedArray.recycle();
            }
        }


        mIndicatorLinePaint.setColor(mIndicatorLineColor);
        mIndicatorLinePaint.setStrokeWidth(10);
        mIndicatorLinePaint.setStyle(Paint.Style.FILL);

        mIndicatorTextPaint.setColor(mDefaultTextColor);
        mIndicatorTextPaint.setStyle(Paint.Style.STROKE);
        mIndicatorTextPaint.setTextSize(mTextSize);

        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    //这里宽高由父ViewGroup负责测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        if (widthMode == MeasureSpec.AT_MOST){
//
//        }
//
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (mScrollModel == SCROLL_MODE_FIXED) {
            mIndicatorLineWidth = getMeasuredWidth() / titleArray.size();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = titleArray.size();
        String text;
        for (int x = 0; x < size; x++) {
            text = titleArray.get(x);
            float mTextWidth = mIndicatorTextPaint.measureText(text);
            canvas.drawText(text, mIndicatorLineWidth * x + mIndicatorLineWidth / 2 - mTextWidth / 2, (getHeight() - mIndicatorLineHeight) / 2 + 5, mIndicatorTextPaint);
        }

        canvas.drawRect(0, getHeight() - mIndicatorLineHeight, mIndicatorLineWidth, getHeight(), mIndicatorLinePaint);
    }

    VelocityTracker velocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == velocityTracker) {
            velocityTracker = VelocityTracker.obtain();
            velocityTracker.addMovement(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityTracker.computeCurrentVelocity(1000, 100);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }
}
