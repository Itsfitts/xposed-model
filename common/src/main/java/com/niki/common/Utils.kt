package com.niki.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlin.reflect.KClass

inline fun <reified A : Activity> Activity.start(intentPreference: Intent.() -> Unit = {}) {
    val i = Intent(this, A::class.java)
    i.intentPreference()
    startActivity(i)
}

fun Context.toast(msg: String) = CoroutineScope(Dispatchers.Main).launch {
    Toast.makeText(this@toast, msg, Toast.LENGTH_SHORT).show()
}

suspend fun <R> count(tag: String, block: suspend () -> R): R {
    val startTime = System.currentTimeMillis()
    val r = block()
    logV("$tag 用时 ${System.currentTimeMillis() - startTime}ms")
    return r
}

inline fun <reified T : Any> getSealedChildren(
    noinline filter: (KClass<out T>) -> T?
): List<T> {
    return getSealedChildren(T::class, filter)
}

fun <T : Any> getSealedChildren(
    sealedClass: KClass<T>,
    filter: (KClass<out T>) -> T?
): List<T> {
    require(sealedClass.isSealed) { "传入的参数必须是封装类" }

    // 递归收集所有子类的函数
    fun collectSubclasses(kClass: KClass<out T>): List<KClass<out T>> {
        return if (kClass.isSealed) {
            // 如果是密封类, 递归收集其直接子类的子类
            kClass.sealedSubclasses.flatMap { collectSubclasses(it) }
        } else {
            // 如果不是密封类, 直接返回自身
            listOf(kClass)
        }
    }

    // 获取所有子类 (包括间接子类), 然后应用 filter
    return collectSubclasses(sealedClass).mapNotNull(filter)
}

private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = false // 根据需要调整，决定输出的 JSON 字符串是否带格式
    encodeDefaults = true // 编码时是否包含默认值
}

fun JsonObject.parseToString(): String {
    return json.encodeToString(this)
}

fun String.parseToJsonObj(): JsonObject? {
    // 直接解析为 JsonElement
    return try {
        json.parseToJsonElement(this).jsonObject
    } catch (_: Throwable) {
        null
    }
}

inline fun <reified T> JsonObject.getAs(key: String): T? {
    val p = get(key)?.jsonPrimitive ?: return null
    val data =
        when (T::class.java) { // 踩过一个坑，when 上面写的是 class.java 下面也要 class.java，不然 kotlin.xx 和 java.lang.xx 匹配不了
            String::class.java -> {
                if (p.isString) {
                    p.contentOrNull
                } else {
                    p.toString()
                }
            }

            Int::class.java -> p.intOrNull
            Long::class.java -> p.longOrNull
            Float::class.java -> p.floatOrNull
            Double::class.java -> p.doubleOrNull
            Boolean::class.java -> p.booleanOrNull
//        Any::class.java -> {}
            else -> null
        }
    return data as? T
}