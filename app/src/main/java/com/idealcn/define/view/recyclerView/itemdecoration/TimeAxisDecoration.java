package com.idealcn.define.view.recyclerView.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.idealcn.define.view.listener.OnTimeAxisChangeListener;

/**
 * author: ideal-gn
 * date: 2017/9/30.
 */

public class TimeAxisDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private OnTimeAxisChangeListener onTimeAxisChangeListener;
    public TimeAxisDecoration(){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);


        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.parseColor("#78ce2a"));
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeWidth(20);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.parseColor("#78ce2a"));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(10);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(22);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(5);


        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.parseColor("#33000000"));

        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(5);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 200;
        outRect.top = 15;
        outRect.bottom = 15;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        paint.setColor(Color.RED);
        int childCount = parent.getChildCount();//这个返回的是界面显示的个数


        for (int x = 0; x < childCount; x++) {
            View child = parent.getChildAt(x);
            RecyclerView.ViewHolder holder = parent.findViewHolderForAdapterPosition(parent.getChildAdapterPosition(child));

            canvas.drawCircle(25,child.getTop()+child.getHeight()/2,5,mPointPaint);
            String time = "09:12:34";
            canvas.drawText(time,70,child.getTop()+child.getHeight()/2,mTextPaint);
//            if (onTimeAxisChangeListener!=null){
//                onTimeAxisChangeListener.onChange(canvas,mTextPaint)
//            }

            canvas.drawLine(25,child.getTop()-15,25,child.getBottom()+15,mLinePaint);
            if (x==childCount-1){
                canvas.drawRect(0,child.getTop()-15,parent.getWidth(),child.getBottom()+15,mBackgroundPaint);
            }
        }

    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);


    }


}
