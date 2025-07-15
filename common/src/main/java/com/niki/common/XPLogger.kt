package com.niki.common

import android.util.Log
import de.robv.android.xposed.XposedBridge

fun logV(msg: String = "") {
    if (AppDebugConfig.LOG_LEVEL <= 0) {
        safeLog("${AppDebugConfig.LOG_HEADER}[详细]: $msg")
    }
}

fun logD(msg: String = "") {
    if (AppDebugConfig.LOG_LEVEL <= 1) {
        safeLog("${AppDebugConfig.LOG_HEADER}[调试]: $msg")
    }
}

fun logE(msg: String = "", t: Throwable? = null) {
    if (AppDebugConfig.LOG_LEVEL <= 2) {
        var s = t?.stackTraceToString()
        if (s != null) {
            s = "\n$s"
        }
        safeLog("${AppDebugConfig.LOG_HEADER}[异常]: $msg${s ?: ""}")
    }
}

/**
 * 在各种情况下都尽可能打日志
 *
 * 这里这么多捕捉是因为很容易 class not found
 */
private fun safeLog(string: String) = try {
    XposedBridge.log(string)
} catch (_: Throwable) {
    try {
        Log.e("XposedBridge", string)
    } catch (_: Throwable) {
        println("XposedBridge: $string")
    }
}