package com.idealcn.define.view.view

import android.content.Context
import android.icu.util.TimeUnit
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewConfigurationCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import com.idealcn.define.view.utils.LoggerUtil
import java.util.logging.Logger

/**
 * 日期: 2018/9/26 10:12
 * author: guoning
 * description:
 */
class ScrollerLayout : FrameLayout {


    val logger: Logger = Logger.getLogger(this.javaClass.simpleName)

    lateinit var dragHelper: ViewDragHelper
    private val scaledTouchSlop: Int


    init {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        dragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(capturedView: View, pointerId: Int): Boolean {
                for (x in 0 until childCount) {
                    val child = getChildAt(x)
                    if (child.visibility == View.GONE) continue
                    if (child == capturedView) return true;
                }
                return false
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                for (x in 0 until childCount) {
                    if (getChildAt(x) == child) {
                        //左边界约束,在ScrollerLayout未发生滑动的情况下,当前触摸的子View距离ScrollerLayout的左边界的距离值.
                        var clampLeft = 0
                        //右边界约束,在ScrollerLayout未发生滑动的情况下,当前触摸的子View距离ScrollerLayout的右边界的距离值.
                        var clampRight = 0
                        for (y in 0 until x) {
                            clampLeft += getChildAt(y).width
                        }
                        for (y in x + 1 until childCount) {
                            clampRight += getChildAt(y).width
                        }
                        //当前触摸的子View距离ScrollerLayout的左边界不能超过clampLeft的约束值,子View向右滑动的极限
                        if (left > clampLeft) return clampLeft
                        //当前触摸的子View距离ScrollerLayout的右边界不能超过clampRight的约束值,子View向左滑动的极限
                        if (left + clampRight < 0) return clampRight
                    }
                }
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }



            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                for (x in 0 until childCount) {
                    if (getChildAt(x) == changedView) {
                        changedView.layout(left, 0, left + changedView.width, height)
                        //当前触摸的子View左右两边的View的left值,也就是距离ScrollerLayout的左边界的距离.
                        var totalChildWidth: Int = 0
                        //对于changedView左侧的View,采用由右至左的顺序来改变每个view的位置.方便totalChildWidth做累加操作
                        for (y in x - 1 downTo 0) {
                            val child = getChildAt(y)
                            totalChildWidth += child.width
                            child.layout(left - totalChildWidth, top, left - (totalChildWidth - child.width), height)
                        }
                        //changedView右侧的第一个View距离ScrollerLayout的左边界的默认距离
                        totalChildWidth = changedView.width+left
                        //对于changedView右侧的,采用由左至右的顺序来改变每个view的位置.
                        for (y in x + 1 until childCount) {
                            val child = getChildAt(y)
                            child.layout(totalChildWidth, 0, child.width + totalChildWidth, height)
                            totalChildWidth += child.width
                        }
                        break
                    }
                }
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                val childCount = childCount
                for (x in 0 until childCount) {
                    if (releasedChild == getChildAt(x)) {
                        val left = releasedChild.left
                        val preChild = getChildAt(x - 1)
                        if (left > 0) {
                            //releaseChild在向右滚动,如果它滚动的距离超过了前一个View宽度的一半,
                            if (left - preChild.width / 2 > 0) {
                                dragHelper.smoothSlideViewTo(releasedChild, preChild.width , 0)
                                var totalChildWidth = -preChild.width
                                for (y in x - 1 downTo 0) {
                                    val view = getChildAt(y)
                                    totalChildWidth += view.width
                                    dragHelper.smoothSlideViewTo(view, -totalChildWidth, 0)
                                }
                                totalChildWidth = preChild.width + releasedChild.width
                                for (y in x + 1 until childCount) {
                                    val view = getChildAt(y)
                                    dragHelper.smoothSlideViewTo(view, totalChildWidth, 0)
                                    totalChildWidth += view.width
                                }
                            } else {
                                dragHelper.smoothSlideViewTo(releasedChild,0, 0)

                                var totalChildWidth = 0
                                for (y in x - 1 downTo 0) {
                                    val view = getChildAt(y)
                                    totalChildWidth += view.width
                                    dragHelper.smoothSlideViewTo(view, -totalChildWidth, 0)
                                }
                                totalChildWidth = releasedChild.width
                                for (y in x + 1 until childCount) {
                                    val view = getChildAt(y)
                                    dragHelper.smoothSlideViewTo(view, totalChildWidth, 0)
                                    totalChildWidth += view.width
                                }
                            }
                        } else {
                            if (left + releasedChild.width / 2 < 0) {
                                dragHelper.smoothSlideViewTo(releasedChild, -releasedChild.width, 0)
                                var totalChildWidth = releasedChild.width
                                for (y in x - 1 downTo 0) {
                                    totalChildWidth += getChildAt(y).width
                                    dragHelper.smoothSlideViewTo(getChildAt(y), -totalChildWidth, 0)
                                }
                                totalChildWidth = 0
                                for (y in x + 1 until childCount) {
                                    val childY = getChildAt(y)
                                    dragHelper.smoothSlideViewTo(childY, totalChildWidth, 0)
                                    totalChildWidth += childY.width
                                }
                            } else {
                                dragHelper.smoothSlideViewTo(releasedChild, 0, 0)
                                var totalChildWidth = 0
                                for (y in x - 1 downTo 0) {
                                    totalChildWidth += getChildAt(y).width
                                    dragHelper.smoothSlideViewTo(getChildAt(y), -totalChildWidth, 0)
                                }
                                totalChildWidth = releasedChild.width
                                for (y in x + 1 until childCount) {
                                    dragHelper.smoothSlideViewTo(getChildAt(y), totalChildWidth, 0)
                                    totalChildWidth += getChildAt(y).width
                                }
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(this@ScrollerLayout)
                        break
                    }
                }
            }


            override fun getViewHorizontalDragRange(child: View): Int {
                var dragRange = 0
                for (x in 0 until childCount) {
                    val child = getChildAt(x)
                    dragRange += child.width
                }
                return dragRange

            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 0
            }

        })
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var pWidth = 0
        var pHeight = 0
        for (x in 0 until childCount) {
            val child = getChildAt(x)
            if (child.visibility == View.GONE) continue

            //child.measure(widthMeasureSpec,heightMeasureSpec) 这样不正确
/*        下面是正确的,
            child.measure(MeasureSpec.makeMeasureSpec(child.measuredWidth, MeasureSpec.EXACTLY)
                  ,MeasureSpec.makeMeasureSpec(child.measuredHeight, MeasureSpec.EXACTLY))
              考虑到继承自FrameLayout,child.measureWidth和child.measureHeight本身已经测量好了,可以直接用
                ,无需再次测量child
                如果继承ViewGroup,需要自己去测量child
                  */
            val params = child.layoutParams as FrameLayout.LayoutParams
            logger.info("params: width: ${params.width},height: ${params.height}")
            pWidth += child.measuredWidth + params.leftMargin + params.rightMargin
            pHeight = Math.max(child.measuredHeight + params.topMargin + params.bottomMargin, pHeight)
        }
        setMeasuredDimension(pWidth / childCount, pHeight)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var lastWidth = 0
        for (x in 0 until childCount) {
            val child = getChildAt(x)
            if (child.visibility == View.GONE) continue
            val layoutParams : FrameLayout.LayoutParams = child.layoutParams as LayoutParams
            child.layout(lastWidth+layoutParams.leftMargin, 0, lastWidth + child.width+layoutParams.leftMargin, height)
            lastWidth += child.width + layoutParams.leftMargin + layoutParams.rightMargin
        }

    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


}