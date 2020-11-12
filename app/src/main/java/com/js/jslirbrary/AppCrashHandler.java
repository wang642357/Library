/*
 *
 *  * Copyright (C) 2018 酒圣科技有限公司
 *  * 版权所有
 *  *
 *  * 功能描述：
 *  * 作者：wangjianxiong
 *  * 创建时间：2018年07月26日
 *  *
 *  * 修改人：
 *  * 修改描述：
 *  * 修改日期
 *
 */

package com.js.jslirbrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

/**
 * Created by wangjianxiong on 2018/3/27.
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;

    private Thread.UncaughtExceptionHandler mHandler;

    private AppCrashHandler() {

    }

    public static final AppCrashHandler getInstance() {
        return SingleHolder.mCrashHandler;
    }

    private static class SingleHolder {
        private static final AppCrashHandler mCrashHandler = new AppCrashHandler();
    }

    public void init(Context context) {
        mContext = context;
        // 获取默认异常处理器
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    @SuppressLint("WrongConstant")
    public void uncaughtException(Thread t, Throwable ex) {
        ex.printStackTrace();
        Log.e("---", "uncaughtException: " + ex.toString());
        if (!handlerException(ex) && mHandler != null) {
            //用户没有处理
            mHandler.uncaughtException(t, ex);
        } else {
           /* SystemClock.sleep(1000);
            //程序闪退后的操作:退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);

            Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
            AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            //重启应用，得使用PendingIntent
            PendingIntent restartIntent = PendingIntent.getActivity(
                    mContext.getApplicationContext(), 0, intent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Android6.0以上，包含6.0
                mAlarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); //解决Android6.0省电机制带来的不准时问题
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //Android4.4到Android6.0之间，包含4.4
                mAlarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 解决set()在api19上不准时问题
            } else {
                mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent);
            }*/
            //ActivityContainer.getInstace().finishAllActivities();
        }

    }

    private boolean handlerException(Throwable e) {
        if (e == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // Toast.makeText(mContext, "足下课堂准备重启！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        //在此可执行其它操作，如获取设备信息、执行异常登出请求、保存错误日志到本地或发送至服务端
        //MobclickAgent.reportError(mContext,e);
        return true;
    }

}
