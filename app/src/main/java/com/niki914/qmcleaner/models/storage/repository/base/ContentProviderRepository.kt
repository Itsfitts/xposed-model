package com.niki914.qmcleaner.models.storage.repository.base

import android.content.Context
import android.database.Cursor
import com.niki914.common.logD
import com.niki914.common.logE
import com.niki914.qmcleaner.models.storage.getCursor

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
}