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
