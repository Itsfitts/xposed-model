package com.niki914.qmcleaner.viewmodels

import com.niki914.common.Key
import com.niki914.qmcleaner.models.storage.repository.SettingsRepository
import com.niki914.qmcleaner.viewmodels.base.BaseMVIViewModel
import com.zephyr.log.logI
import com.zephyr.provider.TAG

sealed class MainIntent {
    data class UpdateValue(val key: Key, val value: Any) : MainIntent()
    data class SaveValue(val key: Key, val value: Any) : MainIntent()
}

data class MainState(
    val placeholder: Int = 0
)

class MainViewModel : BaseMVIViewModel<MainIntent, MainState, Nothing>() {
    override fun initUiState(): MainState {
        return SettingsRepository.getInstance().run {
            MainState(
//                getAPIKey(),
            )
        }
    }

    override fun handleIntent(intent: MainIntent) {
        logI(TAG, "接受 intent: ${intent.javaClass.simpleName}")
        when (intent) {
            is MainIntent.UpdateValue -> {
                updateStateByIntent(intent.key, intent.value)
            }

            is MainIntent.SaveValue -> {
//                val value = if (intent.key is Key.BaseUrl) {
//                    val url = intent.value as String
//                    url.ensureEnding()
//                } else {
//                    intent.value
//                }
                val value = intent.value
                updateStateByIntent(intent.key, value)
                repo.setValueToPref(intent.key, value)
            }

            else -> {}
        }
    }

    override fun updateStateByIntent(key: Key, value: Any) {
        when (key) {
//            Key.ApiKey -> updateState {
//                copy(apiKey = value as String)
//            }

            else -> {}
        }
    }
}