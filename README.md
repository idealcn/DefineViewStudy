canvas.save()和canvas.restore()的区别

canvas.save()是保存当前画布的状态,canvas.restore()是取出画布之前保存的状态

ViewGroup如何决定子view的宽高和测量模式,以高度为例

1. 如果ViewGroup的高度设置为match_parent或者具体数值
   - 子view的高度设置为match_parent或者具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为ViewGroup所给出的最大高度或者具体数值
     - 子view的高度设置为wrap_content,那么子view的测量模式为MeasureSpec.AT_MOST,高度为ViewGroup所给出的最大高度
2. 如果ViewGroup的高度设置为wrap_content
   - 子view的高度设置为具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为具体数值
     - 子view的高度设置为match_parent或者wrap_content,子view的测量模式为MeasureSpec.AT_MOST,也就是说子view的高度不能超过ViewGroup的高度
3. 具体可查看ViewGroup.getChildMeasureSpec()方法的源码

requestLayout和invalidate()

1. requestLayout()会调用ViewParent.requestLayout(),并为重新布局打上标记,这个调用过程采用的责任链模式,最终调用了ViewRootImpl.requestLayout(), 然后调用scheduleTraversals,在这个方法中开启了一个子线程去执行doTraversal(),在这个方法中执行了performTraversals(),然后重新执行三大流程
2. invalidate()最终也是执行到ViewRootImpl的scheduleTraversals,但是这个方法只对重新绘制做了标记，因此不会走onMeasure和onLayout.这是个同步方法，异步执行可以调用postInvalidate().

View的绘制流程

　从ViewRootImpl的performTraveals()方法开始执行.通过getRootMeasureSpec获取顶级DecorView的MeasureSpec,然后执行performMeasure(),performLayout,performDraw.

1. performMeasure();
2. performLayout();----> View.layout();---->View.onLayout();对于单个View,无需重写onLayout();对于ViewGroup，其将onLayout()定义成抽象方法，子类 必须重写.

在Activity里面获取view的宽高时,在onCreate()获取,view可能还没初始化完成,因此获取到的宽高可能为0,通过以下两种方式解决:

1. View.getViewTreeObserver()

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

1. 在onWindowFocusChanged(boolean hasFocus),这个方法当Activity不在前台显示时调用,在onResume之后,onPause之前.
       if(hasFoucs){
           //TODO 获取view宽高
       }
   增加了自定义view

自定义进度条

- 自定义圆形进度条
  canvas.drawArc(Rect,float...);
  所绘制的弧形所在的圆是Rect的内切圆.Rect封装的不是view四个边界的坐标,而是到左边界和上边界的距离.

       //这里应将padding考虑在内,确切的说,一切自定义view在onDraw时,都应将Padding考虑在内.
        rect.set(getPaddingLeft(),getPaddingTop(),getWidth()-getPaddingLeft()-getPaddingRight(),
                        getHeight()-getPaddingTop()-getPaddingBottom());

- 自定义线性进度条

- 加入屏幕适配
    -- 尺寸适配
    分辨率就是像素个数。设备的dpi可以通过DisplayMetrics类来获取。
    ```
        int scale = DisplayMetric.scaleDenisty;//相对于160dpi的缩放倍数
    ```
    布局文件设置的dp大小是以160dpi为基准的。所以根据设计图在布局中设置dp大小时，需要做对应的缩放。
    例如：以480dpi为基准的设计图，某一尺寸为48px。布局中设置的dp大小应当缩小三分之一，为16dp。
    同一尺寸比如60px，在低分辨率屏幕上显示的要比高分辨率的大。
    原因是：高分辨率的屏幕在单位尺寸上显示的像素个数比低分辨率的多。那么显示60个像素所占的屏幕大小就小一些。

    对于尺寸适配，通过系统API获取的尺寸是根据当前屏幕的dpi做了对应转化的，不需要适配，而自己在代码中动态设置的尺寸才需要适配。
    -- 图片适配
        以xhdpi和xxhdpi为基准做两套图基本够用，必要的可以做.9.patch图
    -- 布局适配
        尺寸限定符，宽度限定符，布局别名