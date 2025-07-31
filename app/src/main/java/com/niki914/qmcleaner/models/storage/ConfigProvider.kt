package com.niki914.qmcleaner.models.storage

import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.core.net.toUri
import com.niki914.common.Key
import com.niki914.common.logE
import com.niki914.common.utils.EmptyContentProvider
import com.niki914.common.utils.SharedPreferenceHelper
import com.niki914.qmcleaner.models.storage.repository.SettingsRepository

fun Context.getCursor(key: String): Cursor? {
    val queryUri = ConfigProvider.getQueryUri(key)
    val cursor = contentResolver?.query(queryUri, null, null, null, null)
    return cursor
}

/**
 * content provider 用于让 模块读取配置信息，但由于查询简单可能被其他应用获取到密钥信息
 */
class ConfigProvider : EmptyContentProvider() {
    companion object {
        private const val AUTHORITY = "com.niki914.qmcleaner.config.provider"

        fun getQueryUri(key: String): Uri {
            return "content://${AUTHORITY}/get/$key".toUri()
        }

        private const val CODE_GET_VALUE = 1
        private const val CODE_SET_VALUE = 2
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
        context?.let {
            helper = SharedPreferenceHelper(it, SettingsRepository.PREF_NAME)
            return true
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            CODE_GET_VALUE -> {
                val key = uri.lastPathSegment // 获取 URI 路径中的最后一个段作为键
                // 检查键是否有效且在允许的键列表中
                if (key != null && allowedKeys.contains(key)) {
                    return try {
                        createGetCursor(key)
                    } catch (e: Exception) {
                        logE("ConfigProvider#get", e)
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

    private fun createGetCursor(key: String): Cursor? {
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
    }
}