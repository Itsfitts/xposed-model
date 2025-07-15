package com.niki.hooker.model

import com.niki.common.logD
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * 调试用，列出一个类的所有定义方法、属性和内部类。没有递归
 */
class DeclaredHooker(val className: String) : BaseHooker<Nothing, Unit>() {

    override val TAG: String = "$className>Declared"
    override fun XC_LoadPackage.LoadPackageParam.hookInternal(callback: (Nothing) -> Unit) {
        val targetClass = getClass(className)

        // 遍历并 Hook 目标类的所有方法
        // 注意：这会 Hook 所有方法，包括构造函数、私有方法等
        for (method in targetClass.declaredMethods) {
            val methodName = method.name
            val parameterTypes = method.parameterTypes // 获取参数类型数组
            val returnType = method.returnType
            // 构造参数类型字符串，以便打印
            val paramTypesString = parameterTypes.joinToString(", ") { it.name }
            logD("$className 有方法: $methodName($paramTypesString): $returnType")
        }

        for (clazz in targetClass.declaredClasses) {
            val clazzName = clazz.name
            logD("$className 有内部类: $clazzName")
        }

        for (field in targetClass.declaredFields) {
            logD("$className 有内部类: ${field.name}: ${field::class.java.name}")
        }
    }
}