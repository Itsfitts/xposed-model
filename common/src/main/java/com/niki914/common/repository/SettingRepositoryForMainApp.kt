package com.niki914.common.repository

import com.niki914.common.repository.interfaces.IEditableSettingsRepository
import com.niki914.common.utils.SharedPreferenceHelper

abstract class SettingRepositoryForMainApp(helper: SharedPreferenceHelper) :
    SharedPrefRepository(helper), IEditableSettingsRepository