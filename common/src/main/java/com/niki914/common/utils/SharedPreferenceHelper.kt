package com.niki914.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.niki914.common.Key

/**
 * 帮助类，用于简化 SharedPreferences 的读写操作
 * 使用 datastore 存在模块和主app同时操作的问题，datastore内部抛出异常
 *
 * @param context 上下文对象，通常传入 ApplicationContext
 * @param prefName SharedPreferences 文件的名称
 */
class SharedPreferenceHelper(private val context: Context, private val prefName: String) {

    /**
     * 获取 SharedPreferences 实例
     * 使用 lazy 初始化确保只在第一次访问时创建，并且是单例。
     */
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    /**
     * 插入泛型值到 SharedPreferences 中。
     * 支持 Int, Long, Float, Boolean, String 类型。
     */
    fun <T> put(key: Key, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Int -> editor.putInt(key.keyId, value)
            is Long -> editor.putLong(key.keyId, value)
            is Float -> editor.putFloat(key.keyId, value)
            is Boolean -> editor.putBoolean(key.keyId, value)
            is String -> editor.putString(key.keyId, value)
            else -> return
        }
//        editor.commit()
        editor.apply() // 异步提交更改
    }

    /**
     * 从 SharedPreferences 中获取泛型值。
     * 支持 Int, Long, Float, Boolean, String 类型。
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: Key): T {
        val defaultValue = key.default as T
        return try {

            if (!sharedPreferences.all.keys.any { it == key.keyId }) { // 检查是否存在键
                put(key, defaultValue)
                return defaultValue
            }

            val v = when (defaultValue) {
                is Int -> sharedPreferences.getInt(key.keyId, defaultValue) as? T
                is Long -> sharedPreferences.getLong(key.keyId, defaultValue) as? T
                is Float -> sharedPreferences.getFloat(key.keyId, defaultValue) as? T
                is Boolean -> sharedPreferences.getBoolean(key.keyId, defaultValue) as? T
                is String -> (sharedPreferences.getString(key.keyId, defaultValue)
                    ?: defaultValue) as? T

                else -> null
            }
            v ?: defaultValue
        } catch (t: Throwable) {
            defaultValue
        }
    }
}