package com.idealcn.define.view.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.idealcn.define.view.R;
import com.idealcn.define.view.listener.OnFlowChildClickListener;
import com.idealcn.define.view.utils.DensityUtil;

/**
 * Created by ideal-gn on 2017/9/17.
 * 标签view
 */

public class FlowChildView extends View {

    private Paint paint;
    private String text;
    private int textColor;
    private int textSize;

    private Rect rect = new Rect();
    private   Paint.FontMetrics fontMetrics;


    /* 标签边框 */
    private Paint mBoundaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //标签边框的颜色
    private int boundaryColor = Color.BLACK;
    private int boundaryWidth = 1;
    private   PathEffect pathEffect;


    private OnFlowChildClickListener listener;

    public void setOnFlowChildClickListener(OnFlowChildClickListener listener) {
        this.listener = listener;
    }

    public FlowChildView(Context context) {
        this(context, null);
    }

    public FlowChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowChildView);

        try {
            text = typedArray.getNonResourceString(R.styleable.FlowChildView_text);
            textColor = typedArray.getColor(R.styleable.FlowChildView_textColor, Color.WHITE);
            textSize = typedArray.getDimensionPixelSize(R.styleable.FlowChildView_textSize, DensityUtil.dip2px(context, 14));
            boundaryColor = typedArray.getColor(R.styleable.FlowChildView_boundary_line_color, Color.BLACK);
            boundaryWidth = typedArray.getDimensionPixelSize(R.styleable.FlowChildView_boundary_line_width, DensityUtil.dip2px(context, 1));
        } finally {
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(DensityUtil.dip2px(context, 3));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);


        fontMetrics = paint.getFontMetrics();
//      mTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);//19
//      mTextHeight = (int) (fontMetrics.descent - fontMetrics.ascent);//17
//      float leading = fontMetrics.leading;

        /* 边框 */
        mBoundaryPaint.setColor(boundaryColor);
        mBoundaryPaint.setStrokeWidth(boundaryWidth);
        mBoundaryPaint.setStyle(Paint.Style.STROKE);
        pathEffect = new CornerPathEffect(DensityUtil.dip2px(getContext(), 3));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        paint.getTextBounds(text,0,text.length(),rect);

        /*
        相对于ViewGroup,如果你对自定义的View的尺寸有要求,那么你需要自己去计算;
        而自定义的ViewGroup可以直接调用measure相关方法去测量子view;
        如果是继承自FrameLayout(LinearLayout,RelativeLayout),已经做好了对子view的测量
         */
        int childWidth = getChildWidth(widthMeasureSpec);
        int childHeight = getChildHeight(heightMeasureSpec);


        setMeasuredDimension(childWidth, childHeight);
        rect.set(0,0,childWidth,childHeight);
    }

    private int getChildHeight(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
        }

        if (mode == MeasureSpec.AT_MOST) {
            return Math.min(size,rect.height() + getPaddingTop() + getPaddingBottom());
        }

        return size;
    }

    private int getChildWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            int temp = (int) (paint.measureText(text) + getPaddingLeft() + getPaddingRight());
            return (int) Math.min(size, temp);
        }
        if (mode == MeasureSpec.EXACTLY) {

            return getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制文本居中参考博客：http://blog.csdn.net/hursing/article/details/18703599
        int baseline = (int) ((rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, rect.centerX(), baseline, paint);

        final  int offset = DensityUtil.dip2px(getContext(), 0);

        if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT) {
            canvas.drawRoundRect(getLeft()+offset,
                    getTop()+offset,
                    getRight()-offset,
                    getBottom()-offset,
                    DensityUtil.dip2px(getContext(), 3),
                    DensityUtil.dip2px(getContext(), 3)
                    , mBoundaryPaint);
        }else {
            mBoundaryPaint.setPathEffect(pathEffect);
            canvas.drawRect(getLeft()+offset,
                    getTop()+offset,
                    getRight()-offset,
                    getBottom()-offset,
                    mBoundaryPaint);
        }
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
