package com.js.library.http.error

import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

/**
 * Author：wangjianxiong
 * Date：2020/11/11
 *
 * Desc：
 */
class FlowableRetryDelay(
    val retryConfigProvider: (Throwable) -> RetryConfig
) : Function<Flowable<Throwable>, Publisher<*>> {

    private var retryCount: Int = 0

    override fun apply(throwableFlowable: Flowable<Throwable>): Publisher<*> {
        return throwableFlowable
            .flatMap { error ->
                val (maxRetries, delay, retryTransform) = retryConfigProvider(error)

                if (++retryCount <= maxRetries) {
                    retryTransform()
                        .flatMapPublisher { retry ->
                            if (retry)
                                Flowable.timer(delay.toLong(), TimeUnit.MILLISECONDS)
                            else
                                Flowable.error<Any>(error)
                        }
                } else Flowable.error(error)
            }
    }
}