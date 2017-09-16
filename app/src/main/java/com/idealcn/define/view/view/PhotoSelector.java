package com.idealcn.define.view.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.idealcn.define.view.R;
import com.idealcn.define.view.listener.OnAddPhotoListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ideal-gn on 2017/9/15.
 * 仿微信图片选择器
 */

public class PhotoSelector extends ViewGroup {

    private   int horizontalSpace;
    private int verticalSpace;
    private int columns;
    private float scaledDensity;
    private  View addView;
    private List<View> viewList = new ArrayList<>();
    private AlertDialog dialog;
    private  OnAddPhotoListener listener;


    private int dipToPx(int dpValue){
        return (int) (scaledDensity * dpValue + 0.5f);
    }


    public PhotoSelector(Context context) {
        this(context,null);
    }

    public PhotoSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PhotoSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scaledDensity = getResources().getDisplayMetrics().scaledDensity;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoSelector);


        columns = typedArray.getInt(R.styleable.PhotoSelector_columns, 3);
        horizontalSpace = typedArray.getDimensionPixelSize(R.styleable.PhotoSelector_horizontal_space, dipToPx(5));
        verticalSpace = typedArray.getDimensionPixelSize(R.styleable.PhotoSelector_vertical_space, dipToPx(5));
        typedArray.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);



        int childWidth = (int) ((width - horizontalSpace * columns)/columns + .5f);
        int childHeight = childWidth;

        int childCount = viewList.size();
        for (int x = 0; x < childCount; x++) {
            View child = viewList.get(x);
           PhotoParams lp = (PhotoParams) child.getLayoutParams();
           if (lp==null) {
               lp = new PhotoParams(childWidth, childHeight);
           }

            lp.width = childWidth;
            lp.height = childHeight;
           lp.left = x%columns * childWidth + (x%columns+1) * horizontalSpace;
           lp.top = x/columns * (childHeight + verticalSpace) + verticalSpace;
            child.setLayoutParams(lp);
        }

        if (childCount<=columns)
            setMeasuredDimension(childWidth*childCount,childHeight);
        else {
            int heightCount = childCount%columns!=0?(childCount/columns+1):childCount/columns;
            setMeasuredDimension(childWidth*columns,childHeight*heightCount);
        }
        //测量并确定子view的大小
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = viewList.size();
        for (int x = 0; x < childCount; x++) {
            View child = viewList.get(x);
            PhotoParams lp = (PhotoParams) child.getLayoutParams();
            child.layout(lp.left,lp.top,lp.left + child.getMeasuredWidth() + horizontalSpace,lp.top + child.getMeasuredHeight() + verticalSpace);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView = getChildAt(0);
        viewList.add(addView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        listener = (OnAddPhotoListener) getContext();
        addView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

              final   int size = viewList.size();
                if (size>=8){
                    Toast.makeText(getContext(), "仅能添加7张图片", Toast.LENGTH_SHORT).show();
                    return;
                }


               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setSingleChoiceItems(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[]{"相册", "拍照"})
                        , -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                switch (which){
                                    case 0: {
                                        ImageView view = new ImageView(getContext());
                                        Bitmap bitmap = listener.addByCamera();
                                        view.setImageBitmap(bitmap);
                                        viewList.add(size -1,view);
                                        PhotoSelector.this.addView(view,size-1);
                                        requestLayout();
                                        break;
                                    }
                                    case 1: {
                                        ImageView view = new ImageView(getContext());
                                        Bitmap bitmap = listener.addByGallery();
                                        view.setImageBitmap(bitmap);
                                        viewList.add(size -1,view);
                                        PhotoSelector.this.addView(view,size-1);
                                        requestLayout();
                                        break;
                                    }
                                        default:break;
                                }
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (dialog!=null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PhotoParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new PhotoParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new PhotoParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof PhotoParams;
    }

    public static class PhotoParams extends LayoutParams{

        public int left,top;


        public PhotoParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public PhotoParams(int width, int height) {
            super(width, height);
        }

        public PhotoParams(LayoutParams source) {
            super(source);
        }
    }
}
