# canvas.save()和canvas.restore()的区别
> canvas.save()是保存当前画布的状态,canvas.restore()是取出画布之前保存的状态

# ViewGroup如何决定子view的宽高,以高度为例
> 1. 如果ViewGroup的高度设置为match_parent或者具体数值
        1.1 子view的高度设置为match_parent或者具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为ViewGroup所给出的最大高度或者具体数值
        1.2 子view的高度设置为wrap_content,那么子view的测量模式为MeasureSpec.AT_MOST,高度为ViewGroup所给出的最大高度
  2. 如果ViewGroup的高度设置为wrap_content
        2.1 子view的高度设置为具体数值,那么子view的测量模式为MeasureSpec.EXACTLY,高度为具体数值
        2.2 子view的高度设置为match_parent或者wrap_content,子view的测量模式为MeasureSpec.AT_MOST,也就是说子view的高度不能超过ViewGroup的高度
  3. 具体可查看ViewGroup.getChildMeasureSpec()方法的源码