package com.niki914.hooker

import com.niki914.common.logE
import com.niki914.common.logV
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * hook 操作基类，旨在简化一个 hook 操作和日志的封装
 */
abstract class BaseHooker<T, R> {

    abstract val TAG: String

    fun hook(p: XC_LoadPackage.LoadPackageParam, callback: (T) -> R) {
        tryInternal { p.hookInternal(callback) }
    }

    private fun tryInternal(action: () -> Unit) {
        try {
            logV("Hook $TAG 开始")
            action()
        } catch (e: Exception) {
            logE("Hook $TAG 失败", e)
        } finally {
            logV("Hook $TAG 完成")
        }
    }

    protected open fun XC_LoadPackage.LoadPackageParam.hookInternal(callback: (T) -> R) {}

    protected inline fun <reified T> Any.call(methodName: String, vararg params: Any): T? {
        return XposedHelpers.callMethod(
            this,
            methodName,
            *params
        ) as? T
    }

    // 总是那么写代码太长了
    protected fun XC_LoadPackage.LoadPackageParam.getClass(name: String): Class<*> {
        return XposedHelpers.findClass(name, this.classLoader)
    }

    protected fun XC_LoadPackage.LoadPackageParam.findAndHookMethod(
        className: String,
        methodName: String,
        vararg params: Any?,
        beforeCalled: (param: XC_MethodHook.MethodHookParam?) -> Unit = {},
        afterCalled: (param: XC_MethodHook.MethodHookParam?) -> Unit = {}
    ) {
        XposedHelpers.findAndHookMethod(
            getClass(className),
            methodName,
            *params,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    try {
                        beforeCalled(param)
                    } catch (t: Throwable) {
                        logE("回调 before hooked 出错", t)
                    }
                }

                override fun afterHookedMethod(param: MethodHookParam?) {
                    try {
                        afterCalled(param)
                    } catch (t: Throwable) {
                        logE("回调 after hooked 出错", t)
                    }
                }
            }
        )
    }
}