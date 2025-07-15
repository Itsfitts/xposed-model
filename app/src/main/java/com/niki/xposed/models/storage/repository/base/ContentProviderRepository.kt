package com.niki.xposed.models.storage.repository.base

import android.content.Context
import android.database.Cursor
import com.niki.xposed.models.storage.APIProvider
import com.niki.common.logD
import com.niki.common.logE

open class ContentProviderRepository(private val context: Context) {
    protected fun getString(key: String): String? {
        return context.getCursor(key)?.getValue<String>()
    }

    protected fun getInt(key: String): Int? {
        return context.getCursor(key)?.getValue<Int>()
    }

    protected fun getLong(key: String): Long? {
        return context.getCursor(key)?.getValue<Long>()
    }

    protected fun getFloat(key: String): Float? {
        return context.getCursor(key)?.getValue<Float>()
    }

    protected fun getBoolean(key: String): Boolean? {
        return context.getCursor(key)?.getValue<Boolean>()
    }

    protected inline fun <reified T> Cursor.getValue(): T? {
        try {
            use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex("value")
                    if (columnIndex >= 0) {
                        val v = when (T::class) {
                            String::class -> it.getString(columnIndex)
                            Int::class -> it.getInt(columnIndex)
                            Long::class -> it.getLong(columnIndex)
                            Float::class -> it.getFloat(columnIndex)
                            Boolean::class -> (it.getInt(columnIndex) != 0)   // Boolean 存储为 0/1
                            else -> {
                                logD("ContentProviderRepository#Cursor\$getValue: t is ${T::class.java.name}")
                                null
                            }
                        }
                        return (v as? T)
                    }
                }
            }
        } catch (t: Throwable) {
            logE("ContentProviderRepository#Cursor\$getValue", t)
        }
        return null
    }

//    @Deprecated("系统级上下文由于权限问题不能访问 cp")
//    private fun getSystemContext(p: XC_LoadPackage.LoadPackageParam): Context? {
//        val activityThreadClass =
//            XposedHelpers.findClass("android.app.ActivityThread", p.classLoader)
//
//        // 尝试直接调用静态方法 currentActivityThread() 来获取 ActivityThread 实例
//        // 这个方法在不同Android版本上可能存在差异，甚至可能不是静态方法
//        val activityThreadInstance =
//            XposedHelpers.callStaticMethod(activityThreadClass, "currentActivityThread")
//
//        val systemContext =
//            XposedHelpers.callMethod(activityThreadInstance, "getSystemContext") as? Context
//
//        return systemContext
//    }

    private fun Context.getCursor(key: String): Cursor? {
        val queryUri = APIProvider.getQueryUri(key)
        val cursor = contentResolver?.query(queryUri, null, null, null, null)
        return cursor
    }
}