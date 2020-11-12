package com.js.library.common.rx

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * 作者：wangjianxiong
 * 创建时间：2019-06-05
 *
 * 功能描述：支持Observable、Flowable、Singleable、Completable、Maybe在Android中线程切换
 */
abstract class BaseScheduler<T> protected constructor(
    private val subscribeScheduler: Scheduler,
    private val observerScheduler: Scheduler
) :
    ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(subscribeScheduler)
            .observeOn(observerScheduler)
    }

}