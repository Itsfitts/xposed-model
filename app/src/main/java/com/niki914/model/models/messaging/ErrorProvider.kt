package com.niki914.model.models.messaging

import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.net.Uri
import androidx.core.net.toUri
import com.niki914.common.logE
import com.niki914.common.utils.EmptyContentProvider
import com.niki914.model.MainActivity
import com.niki914.model.R
import com.niki914.model.models.messaging.ErrorProvider.Companion.COLUMN_ERROR_MESSAGE
import com.niki914.model.models.messaging.ErrorProvider.Companion.COLUMN_SHOULD_START_ACTIVITY
import com.niki914.model.models.messaging.ErrorProvider.Companion.COLUMN_STACK_TRACE

fun Context.sendNotificationWithErrorProvider(
    errorMessage: String?,
    stackTrace: String? = null,
    shouldStartActivity: Int = 0
) {
    val queryUri = ErrorProvider.getErrorUri()
    val contentValues = ContentValues().apply {
        put(COLUMN_ERROR_MESSAGE, errorMessage)
        put(COLUMN_STACK_TRACE, stackTrace)
        put(COLUMN_SHOULD_START_ACTIVITY, shouldStartActivity)
    }
    contentResolver?.insert(queryUri, contentValues)
}

// 目的是通过 provider 让模块在出错时发送通知
class ErrorProvider : EmptyContentProvider() {

    companion object {
        private const val AUTHORITY = "com.niki914.model.error.provider"

        // 接收错误信息的 URI
        fun getErrorUri(): Uri = "content://$AUTHORITY/error".toUri()

        // URI 匹配码
        private const val CODE_POST_ERROR = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "error", CODE_POST_ERROR)
        }

        // 用于传递错误信息和堆栈跟踪的键名
        const val COLUMN_ERROR_MESSAGE = "error_message"
        const val COLUMN_STACK_TRACE = "stack_trace"
        const val COLUMN_SHOULD_START_ACTIVITY = "should_start_activity"
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("URI")
        return when (uriMatcher.match(uri)) {
            CODE_POST_ERROR -> {
                val errorMessage = values?.getAsString(COLUMN_ERROR_MESSAGE) ?: "未知错误"
                val stackTraceString = values?.getAsString(COLUMN_STACK_TRACE) ?: ""
                val shouldStartActivity = values?.getAsInteger(COLUMN_SHOULD_START_ACTIVITY) == 1

                val clazz = if (shouldStartActivity) {
                    MainActivity::class.java
                } else {
                    null
                }

                context?.sendNotification(
                    R.drawable.ic_launcher_foreground,
                    "模块发生错误: $errorMessage",
                    stackTraceString,
                    clazz
                )

                logE("收到 Hook 模块的错误: $errorMessage\n堆栈跟踪:\n$stackTraceString")
                uri // 返回接收到的 URI，表明处理成功
            }

            else -> {
                null // 忽略
            }
        }
    }
}