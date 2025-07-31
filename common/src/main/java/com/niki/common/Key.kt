package com.niki.common

import androidx.annotation.StringRes


/**
 * 设置配置项的基本数据类
 *
 * 用的地方超级多，从本地化存储到 UI 都在用
 */
sealed class Key(
    val keyId: String,
    @StringRes val uiStringRes: Int, // 使用资源 ID
    @StringRes val uiDescriptionRes: Int?, // 使用资源 ID
    val default: Any,
    val type: ConfigSettingType
) {

    companion object {
        // 手动维护所有实例的列表
        private val entries: List<Key> = listOf(
//            Url,
        )

        fun getList(): List<String> = entries.map { it.keyId }

        fun getByKeyId(keyId: String): Key? = entries.firstOrNull { it.keyId == keyId }
    }

    /**
     * 配置项的修改方式
     */
    enum class ConfigSettingType {
        INPUT, // 对话框
        SWITCH // 开关
    }

//    data object Url : Key(
//        "api_base_url", // 为了避免丢失就不改这个键了，懒得迁移
//        string.llm_url_ui_string,
//        string.llm_url_ui_description,
//        "https://api.openai.com/v1/chat/completions",
//        ConfigSettingType.INPUT
//    )
}