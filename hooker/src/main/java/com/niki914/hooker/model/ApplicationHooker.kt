package com.niki914.hooker.model

import android.app.Application
import com.niki914.common.logV
import com.niki914.hooker.BaseHooker
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * hook 一个 application 实例
 */
class ApplicationHooker(private val applicationClazzName: String = "android.app.Application") :
    BaseHooker<Application, Unit>() {
    override val TAG: String = "AppApplication#onCreate"

    private fun <T> CancellableContinuation<T>.safeResume(value: T) = runCatching {
        if (isActive) resume(value)
    }

    suspend fun hookBlocking(
        p: XC_LoadPackage.LoadPackageParam,
        timeout: Long = 3000L
    ): Application? {
        return withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine<Application?> { continuation ->
                var called = false
                p.findAndHookMethod(
                    applicationClazzName,
                    "onCreate",
                    afterCalled = { param ->
                        // 确保回调只被执行一次，并且协程只恢复一次
                        if (called || param == null) return@findAndHookMethod
                        (param.thisObject as? Application)?.let { app ->
                            called = true
                            logV("application.onCreate() 获取实例，恢复协程")
                            // 恢复协程并传递 Application 实例
                            continuation.safeResume(app)
                        } ?: run {
                            logV("application.onCreate() 为空，不恢复协程")
                        }
                    }
                )

                // 如果协程被取消，例如因为超时，确保清除钩子或处理资源
                continuation.invokeOnCancellation {
                    logV("ApplicationHooker 协程被取消，可能是超时。")
                    // 这里可以考虑移除钩子，但 Xposed 钩子通常不容易动态移除，
                    // 且对于这种只调用一次的方法，通常不是问题。
                }
            }
        }
    }
}
