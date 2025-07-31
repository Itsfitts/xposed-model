package com.niki914.qmcleaner.viewmodels.base

import com.niki914.common.Key
import com.niki914.qmcleaner.models.storage.repository.SettingsRepository

/**
 * 加一个 repository
 */
abstract class BaseMVIViewModel<Intent, State, Event> :
    ComposeMVIViewModel<Intent, State, Event>() {
    protected val repo by lazy {
        SettingsRepository.getSharedPrefRepositoryInstance()
    }

    /**
     * 应该根据业务范围筛选进行更新的项
     */
    protected abstract fun updateStateByIntent(key: Key, value: Any)

    inline fun <reified T> getValue(key: Key): T {
        return SettingsRepository.getSharedPrefRepositoryInstance().getValueFromPref(key)
    }
}