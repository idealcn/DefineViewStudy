package com.idealcn.define.view.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.idealcn.define.view.R;
import com.idealcn.define.view.utils.LoggerUtil;

public class PositionActivity extends AppCompatActivity {

    private LoggerUtil loggerUtil = LoggerUtil.getInstance();
    private int start = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        final TextView textView = findViewById(R.id.textView);
        findViewById(R.id.offsetLeftAndRight).setOnClickListener(view -> {
            //offsetLeftAndRight这个方法改变了mLeft的值,而没有改变translationX的值
            textView.offsetLeftAndRight(10);
            textView.offsetTopAndBottom(10);
            loggerTextViewPosition(textView);
        });


        findViewById(R.id.valueAnimation).setOnClickListener(view -> {
            //动画改变的是view的translationX和translationY的值,不会改变mLeft和mTop
            ViewPropertyAnimator animate = textView.animate();
            animate.translationYBy(10);
            animate.translationXBy(10);
            animate.start();
            loggerTextViewPosition(textView);
        });


        findViewById(R.id.btnLayout).setOnClickListener(view -> {
            //layout源码中最终在setFrame方法中修改了mLeft的值
            textView.layout(textView.getLeft()+10,textView.getTop()+10,
                    textView.getRight()+10,textView.getBottom()+10);
            loggerTextViewPosition(textView);

        });


        findViewById(R.id.btnTranslation).setOnClickListener(view -> {
            //平移动画未改变mLeft和translationX,并未改变View在布局中的实际位置.
            Animation animation;
            animation = new TranslateAnimation(start,start+100,start,start+100
//                    textView.getX(),textView.getX()+10,
//                    textView.getY(),textView.getY()+10
                    );
            animation.setFillAfter(true);
            animation.setDuration(300);
//            animation = AnimationUtils.loadAnimation(PositionActivity.this,R.anim.anim_translation_textview);
            animation.start();
            textView.startAnimation(animation);
            start += 100;
            loggerTextViewPosition(textView);
        });

        findViewById(R.id.btnLayoutParams).setOnClickListener(view -> {
            //改变了mLeft没有改变translationX,完全改变了View的位置
           ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
           layoutParams.topMargin += 10;
           layoutParams.leftMargin += 10;
            layoutParams.rightMargin -= 10;
            layoutParams.bottomMargin -= 10;
           textView.setLayoutParams(layoutParams);
            ViewCompat.postInvalidateOnAnimation(textView);
            loggerTextViewPosition(textView);
        });


        findViewById(R.id.btnScroll).setOnClickListener(view -> {
            //没有改变mLeft和translationX的值,改变的是View内容的位置,自身位置并没有发生变化.
           ViewGroup parent = (ViewGroup) textView.getParent();
           //
           parent.scrollBy(-10,-10);
           parent.requestLayout();
            loggerTextViewPosition(textView);
        });
    }

    private void loggerTextViewPosition(TextView textView) {
        loggerUtil.info("textView: x: "+textView.getX()+", y: "+textView.getY()+",top: "+textView.getTop()+",left: "+textView.getLeft()
        +",translationX: "+textView.getTranslationX()+",translationY: "+textView.getTranslationY());
    }
}
