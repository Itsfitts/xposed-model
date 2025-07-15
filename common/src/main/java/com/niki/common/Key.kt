package com.niki.common


/**
 * 设置配置项的基本数据类
 *
 * 用的地方超级多，从本地化存储到 UI 都在用
 */
sealed class Key(
    val keyId: String,
    val uiString: String,
    val uiDescription: String?,
    val default: Any,
    val type: ConfigSettingType
) {

    /**
     * 配置项的修改方式
     */
    enum class ConfigSettingType {
        INPUT, // 对话框
        SWITCH // 开关
    }

//    data object EXAMPLEKEY : Key(
//        "shared_pref_key_id",
//        "Example",
//        "description of EXAMPLE",
//        "",
//        ConfigSettingType.INPUT
//    )

    companion object {
        // 手动维护所有实例的列表
        private val entries: List<Key> = listOf(
//            EXAMPLEKEY,
        )

        fun getList(): List<String> = entries.map { it.keyId }

        fun getByKeyId(keyId: String): Key? = entries.firstOrNull { it.keyId == keyId }
    }
}