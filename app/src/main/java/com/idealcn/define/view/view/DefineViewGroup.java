package com.idealcn.define.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * todo : 待解决换行导致显示不全的问题
 */
public class DefineViewGroup extends ViewGroup {

    public DefineViewGroup(Context context) {
        super(context);
    }

    public DefineViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefineViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int pWidth = getWidth(), pHeight = getHeight();
        int childCount = getChildCount();
        int lastChildWidth = 0;
        int lastParentWidth = 0, lastParentHeight = 0;
        int tempLastLineHeight = 0;
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            /*
            在这里使用child.getWidth = 0,child.getHeight = 0,
            Layoutparams只能给出设置了具体数值的属性
            child.getMeasureWidth可以给出宽高
             */
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            if (lastParentWidth + child.getMeasuredWidth() +layoutParams.leftMargin + layoutParams.rightMargin >= pWidth) {
                //换行
                lastParentWidth = 0;
                lastParentHeight += tempLastLineHeight;
            }


            child.layout(layoutParams.leftMargin + lastParentWidth,
                    lastParentHeight+layoutParams.topMargin,
                    layoutParams.leftMargin + lastParentWidth + child.getMeasuredWidth() + layoutParams.rightMargin,
                    lastParentHeight + child.getMeasuredHeight() + layoutParams.bottomMargin);

            lastParentWidth += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            tempLastLineHeight = Math.max(tempLastLineHeight, child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);


        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int widthUsed = 0, heightUsed = 0;
        int tempHeightUsed = 0;
        for (int x = 0; x < childCount; x++) {
            View child = getChildAt(x);
            if (child.getVisibility() == View.GONE) continue;
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            if (widthUsed + child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin >= getMeasuredWidth()) {
                widthUsed = 0;
                heightUsed = tempHeightUsed;
            }
            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
            widthUsed += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            tempHeightUsed = Math.max(tempHeightUsed, child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        }
    }


    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}
