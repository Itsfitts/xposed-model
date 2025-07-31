package com.niki914.common.repository.interfaces

/**
 * 只读的
 */
interface ISettingsRepository {
    fun getAPIKey(): String
    fun getBaseUrl(): String
    fun getModelName(): String
    fun getSystemPrompt(): String
    fun getTimeout(): Long
    fun getProxy(): Pair<String?, Int?>
    fun getEnableApp(): Boolean
    fun getEnableUri(): Boolean
    fun getEnableGetDeviceInfo(): Boolean
    fun getFallbackToBreeno(): String
}