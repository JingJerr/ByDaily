package com.bysofy.bydaily.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.children
import kotlin.math.abs

/**
 *@author: 13173
 *@date: 2021/12/20
 *@description:
 */
public class HorizontalViewKt : ViewGroup {

    private var lastInterceptX: Int = 0
    private var lastInterceptY: Int = 0
    private var lastX: Int = 0
    private var lastY: Int = 0
    private var currentIndex: Int = 0//当前子元素
    private var childWidth = 0
    private lateinit var scroller: Scroller

    //增加速度检测,如果速度比较快的话,就算没有滑动超过一半的屏幕也可以
    private lateinit var tracker: VelocityTracker

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    /**
     * 初始化
     */
    private fun init() {
        scroller = Scroller(context)
        tracker = VelocityTracker.obtain()
    }

    /**
     * 对wrap_content进行处理，继承viewGroup的时候需要对自身设置默认宽高，不然和matchParent效果一样
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        //如果没有子元素，则设置宽和高都为0
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            val childFirst = getChildAt(0)
            val childWidth = childFirst.measuredWidth
            val childHeight = childFirst.measuredHeight
            //如果宽和高都为AT_MOST,则宽度设置为所有子元素宽度的和，高度设置为第一个子元素的高度
            setMeasuredDimension(childWidth * childCount, childHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //如果宽为AT_MOST,则宽度设置为所有子元素宽度的和，高度为heightSize
            val childWidth = getChildAt(0).measuredWidth
            setMeasuredDimension(childWidth * childCount, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //如果高为AT_MOST,则高度设置为第一个子元素的高度，宽度为widthSize
            val childHeight = getChildAt(0).measuredHeight
            setMeasuredDimension(widthSize, childHeight)
        }
    }

    /**
     * 必须实现的抽象方法
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //遍历所有子元素，如果子元素不是GONE，调用layout放置在合适位置上
        var left = 0
        for (child in children) {
            if (child.visibility != View.GONE) {
                val width = child.measuredWidth
                childWidth = width
                //默认第一个元素占满屏幕
                child.layout(left, 0, left + width, child.measuredHeight)
                //后面的元素就在第一个屏幕后面紧挨着的，它的宽度和屏幕大小一样，后续的也是，
                //所以left一直累加，bottom保持为第一个元素的高度
                left += width
            }
        }
    }

    /**
     * 4.滑动冲突的解决
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val x: Int = ev.x.toInt()
        val y: Int = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
                //如果scroller没有执行完毕,则打断
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }
            //水平方向距离长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求
            MotionEvent.ACTION_MOVE -> {
                val deltaX: Int = x - lastInterceptX
                val deltaY: Int = y - lastInterceptY
                //水平方向距离长  MOVE中返回true一次,后续的MOVE和UP都不会收到此请求
                intercept = abs(deltaX) - abs(deltaY) > 0
            }
            MotionEvent.ACTION_UP -> intercept = false
        }
        lastX = x
        lastY = y
        lastInterceptX = x
        lastInterceptY = y
        return intercept

    }

    /**
     * 拦截到之后弹性滑动到其他页面0
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        tracker.addMovement(event)
        //使用Scroller
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }
            //跟随手指滑动
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                //控件移动的方向和屏幕移动的方向互为参照物的时候，方向相反
                //相对于当前View滑动的距离,正为向左,负为向右
                scrollBy(-deltaX, 0)
            }
            //释放手指以后开始自动滑动到目标位置
            MotionEvent.ACTION_UP -> {
                val distance = scrollX - currentIndex * childWidth
                //必须滑动的距离要大于1/2个宽度,否则不会切换到其他页面
                if (abs(distance) > childWidth / 2) {
                    if (distance > 0) {
                        currentIndex++
                    } else {
                        currentIndex--
                    }
                } else {
                    tracker.computeCurrentVelocity(1000)
                    val xV = tracker.xVelocity
                    if (abs(xV) > 50) {
                        if (xV > 0) {
                            currentIndex--
                        } else {
                            currentIndex++
                        }
                    }
                }
                currentIndex = when {
                    currentIndex < 0 -> 0
                    currentIndex > childCount - 1 -> childCount - 1
                    else -> currentIndex
                }
                smoothScrollTo(currentIndex * childWidth, 0)
                tracker.clear()
            }
        }
        lastX = x
        lastY = y
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    //弹性滑动到指定位置
    private fun smoothScrollTo(destX: Int, destY: Int) {
        scroller.startScroll(scrollX, scrollY, destX - scrollX, destY - scrollY, 1000)
        invalidate()
    }
}