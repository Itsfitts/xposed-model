package com.niki.common.repository.interfaces

/**
 * 可写的
 */
interface IEditableSettingsRepository : ISettingsRepository {
    fun setAPIKey(value: String)
    fun setBaseUrl(value: String)
    fun setModelName(value: String)
    fun setSystemPrompt(value: String)
    fun setTimeout(value: Long)
    fun setProxy(host: String?, port: Int?)
    fun setProxy(uri: String)
    fun setEnableApp(value: Boolean)
    fun setEnableUri(value: Boolean)
    fun setEnableGetDeviceInfo(value: Boolean)
    fun setFallbackToBreeno(value: String)
}