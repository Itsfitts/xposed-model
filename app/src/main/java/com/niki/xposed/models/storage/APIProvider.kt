package com.niki.xposed.models.storage

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.niki.common.Key
import com.niki.common.logE
import com.niki.common.utils.SharedPreferenceHelper
import com.zephyr.log.logV

/**
 * content provider 用于让 模块读取配置信息，但由于查询简单可能被其他应用获取到密钥信息
 */
class APIProvider : ContentProvider() {
    companion object {
        private const val AUTHORITY = "com.niki.xposed.api.provider"
        fun getQueryUri(key: String): Uri {
            return Uri.parse("content://${AUTHORITY}/get/$key")
        }

        private const val CODE_GET_VALUE = 1
        private const val CODE_SET_VALUE = 2 // 用于匹配设置值的 URI，但实际操作中不提供写入功能
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "get/*", CODE_GET_VALUE)
            addURI(AUTHORITY, "set", CODE_SET_VALUE)
        }
    }

    private lateinit var helper: SharedPreferenceHelper
    private val allowedKeys = Key.getList()

    /**
     * ContentProvider 创建时调用。初始化 DataStoreHelper。
     */
    override fun onCreate(): Boolean {
//        context?.let {
//            helper = SharedPreferenceHelper(it, SettingsRepository.PREF_NAME) // 待实现
//            return true
//        } ?: return false
        logV("未重实现 APIContentProvider")
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            CODE_GET_VALUE -> {
                val key = uri.lastPathSegment // 获取 URI 路径中的最后一个段作为键
                // 检查键是否有效且在允许的键列表中
                if (key != null && allowedKeys.contains(key)) {
                    try {
                        // 创建 MatrixCursor 来返回查询结果
                        // 定义一个名为 "value" 的列来存储结果
                        val cursor = MatrixCursor(arrayOf("value")).apply {
                            val k = Key.getByKeyId(key) ?: return null
                            val value = helper.get<Any>(k).run {
                                val any = this@run
                                logE("$key - $any - ${any::class.java.name}")
                                if (any is Boolean) {
                                    if (any) 1 else 0 // 布尔用 0/1 存储
                                } else {
                                    any
                                }
                            }
                            addRow(arrayOf(value))
                        }

                        return cursor
                    } catch (e: Exception) {
                        logE("APIProvider#get", e)
                        null
                    }
                } else {
                    null
                }
            }

            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    /**
     * 更新方法，此处不进行实际操作，直接返回 0。
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    /**
     * 删除方法，此处不进行实际操作，直接返回 0。
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    /**
     * 获取 MIME 类型，此处返回 null。
     */
    override fun getType(uri: Uri): String? = null
}