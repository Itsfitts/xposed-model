package com.niki.hooker.model

import com.niki.common.logE
import com.niki.common.logV
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * hook 操作基类，旨在简化一个 hook 操作和日志的封装
 */
abstract class BaseHooker<T, R> {

    protected abstract val TAG: String

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

    protected abstract fun XC_LoadPackage.LoadPackageParam.hookInternal(callback: (T) -> R)

    /**
     * 调用某个对象的某个方法，可传入任意多的参数
     */
    protected fun Any.call(methodName: String, vararg params: Any): Any? {
        val getContentMethod = javaClass.getDeclaredMethod(methodName)
        getContentMethod.isAccessible = true
        return getContentMethod.invoke(this, *params)
    }

    protected inline fun <reified T> Any.callX(methodName: String, vararg params: Any): T? {
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

    protected fun XC_LoadPackage.LoadPackageParam.finAndHookMethod(
        className: String,
        methodName: String,
        vararg params: Any?
    ) {
        XposedHelpers.findAndHookMethod(
            getClass(className),
            methodName,
            *params
        )
    }
}