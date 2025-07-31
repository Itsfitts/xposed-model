package com.niki914.xposed

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.niki914.common.logD
import com.niki914.common.logE
import com.niki914.common.logRelease
import com.niki914.hooker.model.ApplicationHooker
import com.niki914.xposed.models.messaging.sendNotificationWithErrorProvider
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


/**
 * 主hook类，这个需要在 src/main/assets/xposed_init 里面写类名才能被 xposed 识别
 */
class MainHook @Keep() constructor() : IXposedHookLoadPackage {
    companion object {
        /**
         * 使用 BuildConfig，启用混淆后这些属性会自动优化掉，不会泄露
         */
        private object Debug {
            val isDebug = BuildConfig.DEBUG
        }

        const val PACKAGE_NAME = "com.heytap.speechassist"

        private var lastQuery: String? = null
        private var lastCallTime: Long = 0L

        // 防抖动时间。在打开小布主应用后，再用通过电源键唤起小布，这时由于用户输入捕获的实现是通过 UI，
        // 并且 recyclerview 实例化了两个，会导致重复回调，需要去抖动
        private const val DEBOUNCE_DELAY = 150L

        lateinit var BreenoApplication: Application

        lateinit var versionName: String
    }

//    private val repo by lazy { XSettingsRepository.getInstance() }

    private val supervisorContext = SupervisorJob()

    private val hookScope = CoroutineScope(Dispatchers.IO + supervisorContext)

    private val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        logRelease(exception.message ?: "未知错误", exception)
        exception.trySendErrorNotification(1) // 在这里发送错误通知
    }

    /**
     * hook 的触发回调
     */
    @Keep
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookScope.launch(exceptionHandler) {
            logD("正在 Hook: ${lpparam.packageName}")

            TODO("PACKAGE NAME &&& xposed_init ||| use YUKI_HOOK_API")
            if (lpparam.packageName != PACKAGE_NAME) {
                logD("跳过: ${lpparam.packageName}")
                return@launch
            }

            try {
                val application = ApplicationHooker().hookBlocking(lpparam)
                application?.let {
                    versionName = getVersionName(it) ?: ""
                    logE("版本: $versionName")
                    BreenoApplication = it
                    // ...
                } ?: throw Throwable("获取不到目标 context, hook 失败")
            } catch (t: Throwable) {
                logE("主 hook 失败", t)
                t.trySendErrorNotification(1)
            }
        }
    }

    private fun Throwable.trySendErrorNotification(shouldStartActivity: Int = 1) {
        runCatching {
            BreenoApplication.sendNotificationWithErrorProvider(
                message,
                stackTraceToString(),
                shouldStartActivity
            )
        }
    }

    /**
     * 获取自己应用内部的版本名
     */
    private fun getVersionName(context: Context): String? {
        val manager = context.packageManager
        var name: String? = null
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            name = info.versionName
        } catch (e: Exception) {
            logE("版本号获取失败", e)
        }

        return name
    }
}