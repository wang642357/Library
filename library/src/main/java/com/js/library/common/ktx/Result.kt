package com.js.library.common.ktx

import java.io.Serializable

/**
 * 作者：wangjianxiong
 * 创建时间：2020/4/26
 *
 * 功能描述：自定义结果集，成功、失败、加载中的状态
 */
class Result<out T>(val value: Any?) : Serializable {

    /**
     * Returns `true` if this instance represents a successful outcome.
     * In this case [isFailure] returns `false`.
     */
    val isSuccess: Boolean get() = value !is Failure && value !is Loading

    /**
     * Returns `true` if this instance represents a failed outcome.
     * In this case [isSuccess] returns `false`.
     */
    val isFailure: Boolean get() = value is Failure

    val isLoading: Boolean get() = value is Loading

    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    fun loadingOrNull(): Boolean =
        when (value) {
            is Loading -> value.enableCancel
            else -> true
        }

    /**
     * Companion object for [Result] class that contains its constructor functions
     * [success] and [failure].
     */
    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        inline fun <T> success(value: T): Result<T> =
            Result(value)

        inline fun <T> loading(enableCancel: Boolean = true): Result<T> =
            Result(createLoading(enableCancel))

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         */
        inline fun <T> failure(exception: Throwable): Result<T> =
            Result(createFailure(exception))
    }

    class Failure(
        @JvmField
        val exception: Throwable
    ) : Serializable {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception)"
    }

    class Loading(
        @JvmField
        val enableCancel: Boolean
    ) : Serializable {
        override fun equals(other: Any?): Boolean =
            other is Loading && enableCancel == other.enableCancel

        override fun hashCode(): Int = enableCancel.hashCode()
        override fun toString(): String = "Loading($enableCancel)"
    }

}

fun createFailure(exception: Throwable): Any =
    Result.Failure(exception)

fun createLoading(cancel: Boolean): Any =
    Result.Loading(cancel)

@Suppress("UNCHECKED_CAST")
inline fun <R, T> Result<T>.fold(
    onLoading: (enableCancel: Boolean) -> R,
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
    return when {
        isSuccess -> {
            onSuccess(value as T)
        }
        isFailure -> {
            onFailure((value as Result.Failure).exception)
        }
        else -> {
            onLoading((value as Result.Loading).enableCancel)
        }
    }
}

inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let { action(it) }
    return this
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [success][Result.isSuccess].
 * Returns the original `Result` unchanged.
 */
inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (isSuccess) action(value as T)
    return this
}

inline fun <T> Result<T>.onLoading(action: (enableCancel: Boolean) -> Unit): Result<T> {
    action(loadingOrNull())
    //if (isLoading) action(value as Boolean)
    return this
}