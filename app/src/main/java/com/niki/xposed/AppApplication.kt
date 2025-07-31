package com.niki.xposed

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.zephyr.log.LogConfig
import com.zephyr.log.LogLevel

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LogConfig.edit {
            writeToFile = false
            logLevel = LogLevel.DO_NOT_LOG
        }

        DynamicColors.applyToActivitiesIfAvailable(this)
//        SettingsRepository.getInstance(this)
    }
}