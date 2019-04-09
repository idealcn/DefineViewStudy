package com.idealcn.define.view.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class HorizontalRecyclerView extends RecyclerView {

    private PagerSnapHelper snapHelper;


    public HorizontalRecyclerView(Context context) {
        this(context,null);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

     //   setLayoutManager(new GridLayoutManager(context,4,GridLayoutManager.HORIZONTAL,false));

        snapHelper= new PagerSnapHelper(){
          /*  @Override
            public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View targetView) {

                return super.calculateDistanceToFinalSnap(layoutManager, targetView);
            }*/
        };
        snapHelper.attachToRecyclerView(this);
    }


}
