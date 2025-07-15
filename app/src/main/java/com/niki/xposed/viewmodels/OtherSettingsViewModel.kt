//package com.niki.breeno.viewmodels
//
//import com.niki.breeno.models.storage.repository.SettingsRepository
//import com.niki.breeno.viewmodels.base.BaseMVIViewModel
//import com.niki.common.Key
//import com.niki.common.logV
//import com.niki.common.proxyToString
//
//sealed class OSIntent {
//    data class UpdateValue(val key: Key, val value: Any) : OSIntent()
//    data class SaveValue(val key: Key, val value: Any) : OSIntent()
//}
//
//data class OSState(
//    val proxy: String,
//    val enableLaunchApp: Boolean,
//    val enableLaunchUri: Boolean,
//    val enableGetDeviceInfo: Boolean,
//    val fallbackToBreeno: String
//)
//
//class OtherSettingsViewModel : BaseMVIViewModel<OSIntent, OSState, Nothing>() {
//    override fun initUiState(): OSState {
//        return SettingsRepository.getInstance().run {
//            OSState(
//                getProxy().proxyToString(),
//                getEnableApp(),
//                getEnableUri(),
//                getEnableGetDeviceInfo(),
//                getFallbackToBreeno()
//            )
//        }
//    }
//
//    override fun handleIntent(intent: OSIntent) {
//        logV("接受 intent: ${intent.javaClass.simpleName}")
//        when (intent) {
//            is OSIntent.UpdateValue -> {
//                updateStateByIntent(intent.key, intent.value)
//            }
//
//            is OSIntent.SaveValue -> {
//                updateStateByIntent(intent.key, intent.value)
//                repo.setValueToPref(intent.key, intent.value)
//            }
//
//            else -> {}
//        }
//    }
//
//    override fun updateStateByIntent(key: Key, value: Any) {
//        when (key) {
//            Key.Proxy -> updateState {
//                copy(proxy = value as String)
//            }
//
//            Key.EnableLaunchApp -> updateState {
//                copy(enableLaunchApp = value as Boolean)
//            }
//
//            Key.EnableLaunchURI -> updateState {
//                copy(enableLaunchUri = value as Boolean)
//            }
//
//            Key.EnableGetDeviceInfo -> updateState {
//                copy(enableGetDeviceInfo = value as Boolean)
//            }
//
//            Key.FallbackToBreeno -> updateState {
//                copy(fallbackToBreeno = value as String)
//            }
//
//            else -> {}
//        }
//    }
//}