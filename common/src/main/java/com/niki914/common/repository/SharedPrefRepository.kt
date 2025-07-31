package com.niki914.common.repository

import com.niki914.common.Key
import com.niki914.common.utils.SharedPreferenceHelper

open class SharedPrefRepository(val helper: SharedPreferenceHelper) {
    inline fun <reified T> getValueFromPref(key: Key): T {
        return helper.get(key)
    }

    inline fun <reified T> setValueToPref(key: Key, value: T) {
        return helper.put(key, value)
    }
}