//package com.niki.breeno.models.storage.repository
//
//import android.annotation.SuppressLint
//import android.content.Context
//import com.niki.common.Key
//import com.niki.common.logD
//import com.niki.common.parseToProxyPair
//import com.niki.common.repository.SettingRepositoryForMainApp
//import com.niki.common.repository.SharedPrefRepository
//import com.niki.common.repository.interfaces.IEditableSettingsRepository
//import com.niki.common.utils.SharedPreferenceHelper
//
///**
// * 主应用专用
// */
//class SettingsRepository private constructor(helper: SharedPreferenceHelper) :
//    SettingRepositoryForMainApp(helper) {
//
//    companion object {
//        const val PREF_NAME = "niki_breeno_openai_pref"
//
//        @SuppressLint("StaticFieldLeak")
//        @Volatile
//        private var INSTANCE: IEditableSettingsRepository? = null
//
//        /**
//         * 获取 SettingsRepository 的单例实例。
//         * 采用双重检查锁定（Double-Checked Locking）模式，确保线程安全且高效。
//         */
//        fun getInstance(context: Context): IEditableSettingsRepository =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: run {
//                    val helper = SharedPreferenceHelper(context, PREF_NAME)
//                    SettingsRepository(helper).also {
//                        INSTANCE = it
//                        logD("SettingsRepository 已经实例化")
//                    }
//                }
//            }
//
//        fun getInstance(): IEditableSettingsRepository =
//            INSTANCE ?: throw IllegalArgumentException("SettingsRepository 未初始化")
//
//        fun getSharedPrefRepositoryInstance(): SharedPrefRepository =
//            getInstance() as SharedPrefRepository
//    }
//
//    override fun setAPIKey(value: String) {
//        setValueToPref(Key.ApiKey, value)
//    }
//
//    override fun setBaseUrl(value: String) {
//        setValueToPref(Key.Url, value)
//    }
//
//    override fun setModelName(value: String) {
//        setValueToPref(Key.ModelName, value)
//    }
//
//    override fun setSystemPrompt(value: String) {
//        setValueToPref(Key.SystemPrompt, value)
//    }
//
//    override fun setTimeout(value: Long) {
//        setValueToPref(Key.Timeout, value)
//    }
//
//    override fun setProxy(host: String?, port: Int?) {
//        if (host == null || port == null) return
//        setValueToPref(Key.Proxy, "$host:$port")
//    }
//
//    override fun setProxy(uri: String) {
//        val pair = uri.parseToProxyPair()
//        setProxy(pair.first, pair.second)
//    }
//
//    override fun setEnableShowToolCalling(value: Boolean) {
//        setValueToPref(Key.EnableShowToolCalling, value)
//    }
//
//    override fun setEnableApp(value: Boolean) {
//        setValueToPref(Key.EnableLaunchApp, value)
//    }
//
//    override fun setEnableUri(value: Boolean) {
//        setValueToPref(Key.EnableLaunchURI, value)
//    }
//
//    override fun setEnableGetDeviceInfo(value: Boolean) {
//        setValueToPref(Key.EnableGetDeviceInfo, value)
//    }
//
//    override fun setFallbackToBreeno(value: String) {
//        setValueToPref(Key.FallbackToBreeno, value)
//    }
//
//    override fun getAPIKey(): String {
//        return getValueFromPref<String>(Key.ApiKey)
//    }
//
//    override fun getBaseUrl(): String {
//        return getValueFromPref<String>(Key.Url)
//    }
//
//    override fun getModelName(): String {
//        return getValueFromPref<String>(Key.ModelName)
//    }
//
//    override fun getSystemPrompt(): String {
//        return getValueFromPref<String>(Key.SystemPrompt)
//    }
//
//    override fun getTimeout(): Long {
//        return getValueFromPref<Long>(Key.Timeout)
//    }
//
//    override fun getProxy(): Pair<String?, Int?> {
//        val proxyString = getValueFromPref<String>(Key.Proxy) // 如 ${string}:$int
//        return proxyString.parseToProxyPair()
//    }
//
//    override fun getEnableApp(): Boolean {
//        return getValueFromPref<Boolean>(Key.EnableLaunchApp)
//    }
//
//    override fun getEnableShowToolCalling(): Boolean {
//        return getValueFromPref<Boolean>(Key.EnableShowToolCalling)
//    }
//
//    override fun getEnableUri(): Boolean {
//        return getValueFromPref<Boolean>(Key.EnableLaunchURI)
//    }
//
//    override fun getEnableGetDeviceInfo(): Boolean {
//        return getValueFromPref<Boolean>(Key.EnableGetDeviceInfo)
//    }
//
//    override fun getFallbackToBreeno(): String {
//        return getValueFromPref<String>(Key.FallbackToBreeno)
//    }
//}