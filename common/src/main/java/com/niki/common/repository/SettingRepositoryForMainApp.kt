package com.niki.common.repository

import com.niki.common.repository.interfaces.IEditableSettingsRepository
import com.niki.common.utils.SharedPreferenceHelper

abstract class SettingRepositoryForMainApp(helper: SharedPreferenceHelper) :
    SharedPrefRepository(helper), IEditableSettingsRepository