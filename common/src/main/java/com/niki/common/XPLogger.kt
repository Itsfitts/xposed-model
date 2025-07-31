package com.niki.common

import android.util.Log
import com.niki.common.AppDebugConfig.LOG_HEADER
import com.niki.common.AppDebugConfig.LOG_LEVEL
import de.robv.android.xposed.XposedBridge

object AppDebugConfig {
    val isDebug = BuildConfig.DEBUG

    const val ALL = -1
    const val VERBOSE = 0
    const val DEBUG = 1
    const val ERROR = 2
    const val NONE = 3

    val LOG_LEVEL: Int = if (isDebug) {
        ALL
    } else {
        NONE
    }
    const val LOG_HEADER = "[niki]"
}

fun logV(msg: String = "") {
    if (LOG_LEVEL <= 0) {
        safeLog("${LOG_HEADER}[详细]: $msg")
    }
}

fun logD(msg: String = "") {
    if (LOG_LEVEL <= 1) {
        safeLog("${LOG_HEADER}[调试]: $msg")
    }
}

fun logE(msg: String = "", t: Throwable? = null) {
    if (LOG_LEVEL <= 2) {
        var s = t?.stackTraceToString()
        if (s != null) {
            s = "\n$s"
        }
        safeLog("${LOG_HEADER}[异常]: $msg${s ?: ""}")
    }
}

fun logRelease(msg: String = "", t: Throwable? = null) {
    var s = t?.stackTraceToString()
    if (s != null) {
        s = "\n$s"
    }
    safeLog("${LOG_HEADER}: $msg${s ?: ""}")
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