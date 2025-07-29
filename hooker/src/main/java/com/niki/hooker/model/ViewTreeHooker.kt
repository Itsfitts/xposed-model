package com.niki.hooker.model

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.niki.common.logD
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * 通过hook activity的onCreate方法实现遍历视图树，这在hook UI 和 找类上非常方便
 *
 * claude 写的
 */
class ViewTreeHooker(val delay: Long) : BaseHooker<Nothing, Unit>() {
    override val TAG: String = ""
    override fun XC_LoadPackage.LoadPackageParam.hookInternal(callback: (Nothing) -> Unit) {
        findAndHookMethod(
            "android.app.Activity",
            "onCreate",
            Bundle::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject

                    // 延迟遍历视图树
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        traverseViewTree(activity)
                    }, delay)
                }
            }
        )
    }

    private fun traverseViewTree(activity: Any) {
        try {
            val window = activity.call("getWindow") ?: return
            val decorView = window.call("getDecorView") as? ViewGroup ?: return

            logD("开始遍历视图树(${activity.javaClass.simpleName}):")
            traverseViews(decorView, 0)
        } catch (e: Exception) {
            logD("遍历视图树失败: ${e.message}")
        }
    }

    private fun traverseViews(view: View, depth: Int) {
        val indent = "   ".repeat(depth)
        val info = "$indent${getViewDescription(view)} [${getViewId(view)}]"
        logD(info)

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                traverseViews(view.getChildAt(i), depth + 1)
            }
        }
    }

    private fun getViewDescription(view: View): String {
        return when (view) {
            is Button -> "Button(${view.text})"
            is TextView -> "TextView(${view.text})"
            is ImageView -> "ImageView"
            else -> view.javaClass.simpleName
        }
    }

    private fun getViewId(view: View): String {
        return try {
            if (view.id != View.NO_ID) {
                val resources = view.resources
                val idName = resources.getResourceEntryName(view.id)
                val packageName = resources.getResourcePackageName(view.id)
                "$packageName:id/$idName"
            } else {
                "NO_ID"
            }
        } catch (e: Exception) {
            "ID_ERROR"
        }
    }
}