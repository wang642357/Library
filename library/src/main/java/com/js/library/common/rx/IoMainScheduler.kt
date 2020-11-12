package com.js.library.common.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 作者：wangjianxiong
 * 创建时间：2019-06-05
 *
 * 功能描述：Android中的线程调度器
 */
class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())