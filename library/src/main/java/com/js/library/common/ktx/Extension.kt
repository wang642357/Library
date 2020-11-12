package com.js.library.common.ktx

import android.content.Context
import android.util.TypedValue
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.js.library.common.util.ToastUtils

/**
 * dp to px
 */
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

inline fun Context.toast(message: CharSequence) {
    ToastUtils.showShort(message)
}

fun Context.longToast(message: CharSequence) {
    ToastUtils.showShort(message)
}

fun Context.toast(@StringRes message: Int) {
    ToastUtils.showShort(message)
}

fun Context.longToast(@StringRes message: Int) {
    ToastUtils.showShort(message)
}


fun Fragment.toast(message: CharSequence) {
    ToastUtils.showShort(message)
}

fun Fragment.longToast(message: CharSequence) {
    ToastUtils.showShort(message)
}


fun Fragment.toast(@StringRes message: Int) {
    ToastUtils.showShort(message)
}

fun Fragment.longToast(@StringRes message: Int) {
    ToastUtils.showShort(message)
}