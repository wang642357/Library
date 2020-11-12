package com.js.library.common.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 作者：wangjianxiong
 * 创建时间：2019-06-05
 *
 * 功能描述：调度工具类
 */
object SchedulerUtil {

    val ui: Scheduler
        get() = AndroidSchedulers.mainThread()

    val io: Scheduler
        get() = Schedulers.io()

    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }

}