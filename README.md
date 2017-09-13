# canvas.save()和canvas.restore()的区别
> canvas.save()是保存当前画布的状态,canvas.restore()是取出画布之前保存的状态

# ViewGroup如何决定子view的宽高和测量模式,以高度为例
> 1. 如果ViewGroup的高度设置为match_parent或者具体数值
        1.1 子view的高度设置为match_parent或者具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为ViewGroup所给出的最大高度或者具体数值
        1.2 子view的高度设置为wrap_content,那么子view的测量模式为MeasureSpec.AT_MOST,高度为ViewGroup所给出的最大高度
 > 2. 如果ViewGroup的高度设置为wrap_content
        2.1 子view的高度设置为具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为具体数值
        2.2 子view的高度设置为match_parent或者wrap_content,子view的测量模式为MeasureSpec.AT_MOST,也就是说子view的高度不能超过ViewGroup的高度
  > 3. 具体可查看ViewGroup.getChildMeasureSpec()方法的源码

  # requestLayout和invalidate()
  > 1.  requestLayout()会调用ViewParent.requestLayout(),并为重新布局打上标记,这个调用过程采用的责任链模式,最终调用了ViewRootImpl.requestLayout(),
  >      然后调用scheduleTraversals,在这个方法中开启了一个子线程去执行doTraversal(),在这个方法中执行了performTraversals(),然后重新执行三大流程
  > 2. invalidate()最终也是执行到ViewRootImpl的scheduleTraversals,但是这个方法只对重新绘制做了标记，因此不会走onMeasure和onLayout.这是个同步方法，异步执行可以调用postInvalidate().


# View的绘制流程
>　从ViewRootImpl的performTraveals()方法开始执行.通过getRootMeasureSpec获取顶级DecorView的MeasureSpec,然后执行performMeasure(),performLayout,performDraw.
> 1. performMeasure();
> 2. performLayout();----> View.layout();---->View.onLayout();对于单个View,无需重写onLayout();对于ViewGroup，其将onLayout()定义成抽象方法，子类
        必须重写.



# 在Activity里面获取view的宽高时,在onCreate()获取,view可能还没初始化完成,因此获取到的宽高可能为0,通过以下两种方式解决:
    > 1. View.getViewTreeObserver()
    ```
    final  ViewTreeObserver treeObserver = myView.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    if (treeObserver.isAlive()){
                        treeObserver.removeOnGlobalLayoutListener(this);
                        //TODO 获取view宽高

                    }
                }
            });
    ```
    > 2. 在onWindowFocusChanged(boolean hasFocus),这个方法当Activity不在前台显示时调用,在onResume之后,onPause之前.
    ```
    if(hasFoucs){
        //TODO 获取view宽高
    }
    ```
>
    # 增加了自定义view
    1. 自定义进度条
        * 自定义圆形进度条
            canvas.drawArc(Rect,float...);
            所绘制的弧形所在的圆是Rect的内切圆.Rect封装的不是view四个边界的坐标,而是到左边界和上边界的距离.
           ```
           //这里应将padding考虑在内,确切的说,一切自定义view在onDraw时,都应将Padding考虑在内.
            rect.set(getPaddingLeft(),getPaddingTop(),getWidth()-getPaddingLeft()-getPaddingRight(),
                            getHeight()-getPaddingTop()-getPaddingBottom());
            ```
        * 自定义线性进度条

# 加入屏幕适配
