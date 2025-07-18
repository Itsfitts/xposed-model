package com.niki.xposed

import android.app.Application
import com.niki.common.count
import com.niki.common.logD
import com.niki.common.logE
import com.niki.hooker.model.ApplicationHooker
import com.zephyr.log.logV
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
        const val TARGET_PACKAGE_NAME = "com.x.x" // TODO
        lateinit var targetApplication: Application
    }

//    private val repo by lazy { XSettingsRepository.getInstance() } // 应该根据需求重实现 repository

    private val hookScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private fun getNextDayTimeMills(timestamp: Long): Long {
        return (timestamp + 9_0000) * 1000
    }

    /**
     * hook 的触发回调
     */
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE_NAME) {
            logV("略过: ${lpparam.packageName}")
            return
        }

        logD("正在 Hook: ${lpparam.packageName}")

        /**
         * https://www.xbgjw.com/timestamp
         */
        if (System.currentTimeMillis() > getNextDayTimeMills(1752805183)) {
            logE("过期应用")
            return
        }

        hookScope.launch(Dispatchers.IO) {
            try {
                val application = count("阻塞获取 context") {
                    ApplicationHooker(TARGET_PACKAGE_NAME).hookBlocking(lpparam)
                }
                application?.let {
                    targetApplication = it

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