package com.js.jslirbrary

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * 作者：wangjianxiong
 * 创建时间：2020/11/11
 *
 *
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCrashHandler.getInstance().init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}