package com.idealcn.define.view.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import java.util.logging.Logger

/**
 * 日期: 2018/9/26 10:12
 * author: guoning
 * description:
 */
class ScrollerLayout : FrameLayout {


    val logger: Logger = Logger.getLogger(this.javaClass.simpleName)

    lateinit var dragHelper: ViewDragHelper


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
                        var clampLeft = 0
                        var clampRight = 0
                        for (y in 0 until x) {
                            clampLeft += getChildAt(y).width
                        }
                        for (y in x + 1 until childCount) {
                            clampRight += getChildAt(y).width
                        }
                        if (left > clampLeft) return clampLeft
                        if (left + clampRight < 0) return clampRight
                    }
                }
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                super.onViewCaptured(capturedChild, activePointerId)
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                for (x in 0 until childCount) {
                    if (getChildAt(x) == changedView) {
                        changedView.layout(left, 0, left + changedView.width, height)
                        var totalChildWidth: Int = 0
                        for (y in x - 1 downTo 0) {
                            val child = getChildAt(y)
                            totalChildWidth += child.width
                            child.layout(left - totalChildWidth, top, left - (totalChildWidth - child.width), height)
                        }
                        totalChildWidth = 0
                        for (y in x + 1 until childCount) {
                            val child = getChildAt(y)
                            totalChildWidth += child.width
                            child.layout(left + changedView.width + (totalChildWidth - child.width), 0, left + changedView.width + totalChildWidth, height)
                        }
                        break
                    }
                }
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                val left = releasedChild.left
                val childCount = childCount
                for (x in 0 until childCount) {
                    val child = getChildAt(x)
                    if (releasedChild == child) {
                        if (left > 0) {
                            if (left - releasedChild.width / 2 > 0) {
                                dragHelper.smoothSlideViewTo(releasedChild, getChildAt(x - 1).width, 0)
                                var totalChildWidth = -getChildAt(x - 1).width
                                for (y in x - 1 downTo 0) {
                                    totalChildWidth += getChildAt(y).width
                                    dragHelper.smoothSlideViewTo(getChildAt(y), -totalChildWidth, 0)
                                }
                                totalChildWidth = getChildAt(x - 1).width + releasedChild.width
                                for (y in x + 1 until childCount) {
                                    dragHelper.smoothSlideViewTo(getChildAt(y), totalChildWidth, 0)
                                    totalChildWidth += getChildAt(y).width
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
                        break
                    }
                }
                ViewCompat.postInvalidateOnAnimation(this@ScrollerLayout)
            }

            override fun onViewDragStateChanged(state: Int) {
                super.onViewDragStateChanged(state)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                var dragRange = 0
                for (x in 0 until childCount) {
                    dragRange += getChildAt(x).width
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
            logger.info("child.measuredHeight: ${child.measuredHeight}," +
                    " child.measuredWidth : ${child.measuredWidth}")
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
            pWidth += child.measuredWidth
            pHeight = Math.max(child.measuredHeight, pHeight)
        }
        setMeasuredDimension(pWidth / childCount, pHeight)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var lastWidth = 0
        for (x in 0 until childCount) {
            val child = getChildAt(x)
            if (child.visibility == View.GONE) continue
            child.layout(lastWidth, 0, lastWidth + child.width, height)
            lastWidth += child.width
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