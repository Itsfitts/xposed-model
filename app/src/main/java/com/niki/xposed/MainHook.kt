package com.niki.xposed

import android.app.Application
import com.niki.common.count
import com.niki.common.logD
import com.niki.common.logE
import com.niki.hooker.model.ApplicationHooker
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


/**
 * 主hook类，这个需要在 src/main/assets/xposed_init 里面写类名才能被 xposed 识别
 *
 * TODO: 如果修改包名记得去 src/main/assets/xposed_init 里面改
 */
class MainHook : IXposedHookLoadPackage {
    companion object {
        const val TARGET_PACKAGE_NAME = "com.x.x"
        lateinit var TargetApplication: Application
    }

//    private val repo by lazy { XSettingsRepository.getInstance() } // 应该根据需求重实现 repository

    private val hookScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * hook 的触发回调
     */
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookScope.launch(Dispatchers.IO) {
            if (lpparam.packageName != TARGET_PACKAGE_NAME) {
                return@launch
            }

            logD("正在 Hook: ${lpparam.packageName}")

            try {
                val application = count("阻塞获取 context") {
                    ApplicationHooker().hookBlocking(lpparam)
                }
                application?.let {
                    TargetApplication = it

//                    XSettingsRepository.getInstance(it)

                    lpparam.apply {
                        // do shit
                    }
                } ?: logE("获取不到目标 context, hook 失败")
            } catch (t: Throwable) {
                logE("主 hook 失败", t)
            }
        }
    }


//    private fun XC_LoadPackage.LoadPackageParam.hookExample() {
//        XXXHooker().hook(this) { callbackData ->
//        }
//    }
}