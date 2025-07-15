package com.niki.xposed

import android.app.Application
import com.google.android.material.color.DynamicColors

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
//        SettingsRepository.getInstance(this) // 重实现
    }
}