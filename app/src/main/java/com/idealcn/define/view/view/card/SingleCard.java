package com.idealcn.define.view.view.card;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class SingleCard {
    public RectF area;
    private Bitmap bitmap;
    private Paint paint = new Paint();

    public SingleCard(RectF area) {
        this.area = area;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas  canvas){
        canvas.drawBitmap(bitmap, null,area,paint);
    }
}
