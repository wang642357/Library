package com.js.library.base.fragment

import androidx.fragment.app.Fragment

/**
 * Author：wangjianxiong
 * Date：2020/11/11
 *
 * 子类实现
 * 适用于Androidx
 */
abstract class BaseLazyFragment : Fragment() {
    private var isComplete: Boolean = false

    /**
     * 懒加载标识
     */
    private var isFirstLoad: Boolean = true


    override fun onResume() {
        super.onResume()
        isComplete = true
        preLazyLoad()
    }

    private fun preLazyLoad() {
        if (isFirstLoad && isComplete) {
            lazyLoad()

            isFirstLoad = false
        }
    }

    /**
     * 当页面可见时执行该方法
     */
    abstract fun lazyLoad()
}