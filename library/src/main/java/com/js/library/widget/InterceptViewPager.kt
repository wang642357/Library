package com.js.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 作者：wangjianxiong
 * 创建时间：2020/7/6
 *
 * 可以拦截左右滑动的viewpager
 */
class InterceptViewPager @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    ViewPager(context, attr) {

    var isScroll: Boolean = false

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            if (isScroll) {
                super.onInterceptTouchEvent(ev)
            } else {
                false
            }
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (isScroll) {
            super.onTouchEvent(ev)
        } else {
            true
        }
    }
}