package com.niki.xposed.viewmodels.base

import com.niki.common.Key
import com.niki.common.repository.interfaces.IEditableSettingsRepository

/**
 * 加一个 repository
 */
abstract class BaseMVIViewModel<Intent, State, Event> :
    ComposeMVIViewModel<Intent, State, Event>() {
    protected val repo: IEditableSettingsRepository by lazy {
//        SettingsRepository.getAsSharedPrefRepository()
        TODO()
    }

    /**
     * 应该根据业务范围筛选进行更新的项
     */
    protected abstract fun updateStateByIntent(key: Key, value: Any)

    inline fun <reified T> getValue(key: Key): T {
        return TODO()
//        return SettingsRepository.getAsSharedPrefRepository().getValueFromPref(key)
    }
}