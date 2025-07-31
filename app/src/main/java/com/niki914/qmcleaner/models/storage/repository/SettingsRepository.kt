package com.niki914.qmcleaner.models.storage.repository

import android.annotation.SuppressLint
import android.content.Context
import com.niki914.common.logD
import com.niki914.common.repository.SettingRepositoryForMainApp
import com.niki914.common.repository.SharedPrefRepository
import com.niki914.common.repository.interfaces.IEditableSettingsRepository
import com.niki914.common.utils.SharedPreferenceHelper

/**
 * 主应用专用
 */
class SettingsRepository private constructor(helper: SharedPreferenceHelper) :
    SettingRepositoryForMainApp(helper) {

    companion object {
        const val PREF_NAME = "niki914_shared_pref"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: IEditableSettingsRepository? = null

        /**
         * 获取 SettingsRepository 的单例实例。
         * 采用双重检查锁定（Double-Checked Locking）模式，确保线程安全且高效。
         */
        fun getInstance(context: Context): IEditableSettingsRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val helper = SharedPreferenceHelper(context, PREF_NAME)
                    SettingsRepository(helper).also {
                        INSTANCE = it
                        logD("SettingsRepository 已经实例化")
                    }
                }
            }

        fun getInstance(): IEditableSettingsRepository =
            INSTANCE ?: throw IllegalArgumentException("SettingsRepository 未初始化")

        fun getSharedPrefRepositoryInstance(): SharedPrefRepository =
            getInstance() as SharedPrefRepository
    }

//    override fun setAPIKey(value: String) {
//        setValueToPref(Key.ApiKey, value)
//    }

//    override fun getAPIKey(): String {
//        return getValueFromPref<String>(Key.ApiKey)
//    }
}