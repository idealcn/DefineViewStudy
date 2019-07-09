package com.idealcn.define.view.view.card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class MultiCardsView extends View {

    private ArrayList<SingleCard> cardList = new ArrayList<>(5);

    private boolean enableOverdrawOpt = true;



    public MultiCardsView(Context context) {
        this(context,null,0);
    }

    public MultiCardsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MultiCardsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addCards(SingleCard card){
        cardList.add(card);
    }

    public void enableOverdrawOpt(boolean enableOrNot){
        this.enableOverdrawOpt = enableOrNot;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null==cardList || canvas == null){
            return;
        }

        Rect clipBounds = canvas.getClipBounds();
        if (enableOverdrawOpt){
            drawCardsWithoutOverDraw(canvas,cardList.size()-1);
        }else {
            drawCardsNormal(canvas,cardList.size()-1);
        }

    }

    private void drawCardsNormal(Canvas canvas, int index) {
        if (null==canvas || index < 0 || index > cardList.size())return;
        SingleCard singleCard = cardList.get(index);
        if (null!=singleCard){
            drawCardsNormal(canvas,index-1);
            singleCard.draw(canvas);
        }
    }

    private void drawCardsWithoutOverDraw(Canvas canvas, int index) {
        if (null==canvas || index < 0 || index > cardList.size())return;
        SingleCard singleCard = cardList.get(index);

        if (null!=singleCard && !canvas.quickReject(singleCard.area,Canvas.EdgeType.BW)){
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            if (canvas.clipRect(singleCard.area, Region.Op.DIFFERENCE)){
                drawCardsWithoutOverDraw(canvas,index-1);
            }
            canvas.restoreToCount(saveCount);
            saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            if (canvas.clipRect(singleCard.area)){
                Rect clip = canvas.getClipBounds();
                singleCard.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }else {
            drawCardsWithoutOverDraw(canvas,index-1);
        }
    }




}
