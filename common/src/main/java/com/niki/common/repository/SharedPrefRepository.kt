package com.niki.common.repository

import com.niki.common.Key
import com.niki.common.utils.SharedPreferenceHelper

open class SharedPrefRepository(val helper: SharedPreferenceHelper) {
    inline fun <reified T> getValueFromPref(key: Key): T {
        return helper.get(key)
    }

    inline fun <reified T> setValueToPref(key: Key, value: T) {
        return helper.put(key, value)
    }
}