//package com.niki.breeno.viewmodels
//
//import com.niki.breeno.models.storage.repository.SettingsRepository
//import com.niki.breeno.viewmodels.base.BaseMVIViewModel
//import com.niki.common.Key
//import com.zephyr.log.logI
//import com.zephyr.provider.TAG
//
//sealed class MainIntent {
//    data class UpdateValue(val key: Key, val value: Any) : MainIntent()
//    data class SaveValue(val key: Key, val value: Any) : MainIntent()
//}
//
//data class MainState(
//    val apiKey: String,
//    val baseUrl: String,
//    val modelName: String,
//    val systemPrompt: String,
//    val timeout: Long
//)
//
//class MainViewModel : BaseMVIViewModel<MainIntent, MainState, Nothing>() {
//    override fun initUiState(): MainState {
//        return SettingsRepository.getInstance().run {
//            MainState(
//                getAPIKey(),
//                getBaseUrl(),
//                getModelName(),
//                getSystemPrompt(),
//                getTimeout()
//            )
//        }
//    }
//
//    override fun handleIntent(intent: MainIntent) {
//        logI(TAG, "接受 intent: ${intent.javaClass.simpleName}")
//        when (intent) {
//            is MainIntent.UpdateValue -> {
//                updateStateByIntent(intent.key, intent.value)
//            }
//
//            is MainIntent.SaveValue -> {
//                val value = if (intent.key is Key.BaseUrl) {
//                    val url = intent.value as String
//                    url.ensureEnding()
//                } else {
//                    intent.value
//                }
//                updateStateByIntent(intent.key, value)
//                repo.setValueToPref(intent.key, value)
//            }
//
//            else -> {}
//        }
//    }
//
//    override fun updateStateByIntent(key: Key, value: Any) {
//        when (key) {
//            Key.ApiKey -> updateState {
//                copy(apiKey = value as String)
//            }
//
//            Key.BaseUrl -> updateState {
//                copy(baseUrl = value as String)
//            }
//
//            Key.ModelName -> updateState {
//                copy(modelName = value as String)
//            }
//
//            Key.SystemPrompt -> updateState {
//                copy(systemPrompt = value as String)
//            }
//
//            Key.Timeout -> updateState {
//                copy(timeout = value as Long)
//            }
//
//            else -> {}
//        }
//    }
//
//    private fun String.ensureEnding(): String {
//        val str = this
//        if (str.isNotBlank() && !str.endsWith("/"))
//            return "$str/"
//        return this
//    }
//}