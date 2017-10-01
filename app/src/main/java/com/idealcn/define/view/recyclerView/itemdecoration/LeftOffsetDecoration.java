package com.idealcn.define.view.recyclerView.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author: ideal-gn
 * date: 2017/9/30.
 */

public class LeftOffsetDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    public LeftOffsetDecoration(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#55000000"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 50;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int x = 0; x < childCount; x++) {

            View child = parent.getChildAt(x);
            c.drawLine(200,child.getBottom()+15,parent.getWidth(),child.getBottom()+16,paint);

        }
    }
}
