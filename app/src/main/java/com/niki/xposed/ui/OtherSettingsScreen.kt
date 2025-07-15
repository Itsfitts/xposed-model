//package com.niki.breeno.ui
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.selection.toggleable
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.niki.breeno.R.string
//import com.niki.breeno.viewmodels.OSIntent
//import com.niki.breeno.viewmodels.OtherSettingsViewModel
//import com.niki.common.Key
//import com.niki.common.parseToProxyPair
//
//@Composable
//fun OtherSettingsScreen(
//    viewModel: OtherSettingsViewModel,
//    onBack: () -> Unit
//) {
////    val context = getContext()
//
//    val state = viewModel.uiState.collectAsStateWithLifecycle()
//    val proxy = state.value.proxy
//
//    Scaffold(
//        topBar = { SettingTopBar(onBack) }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState()) // 允许内容滚动
//                .fillMaxSize()
//        ) {
//            StringSettingItem(
//                key = Key.Proxy,
//                currentValue = proxy,
//                errorMsg = stringResource(string.proxy_error_msg),
//                onChange = { str ->
//                    viewModel.sendIntent(OSIntent.SaveValue(Key.Proxy, str))
//                },
//                validator = { str ->
//                    val pair = str.parseToProxyPair()
//                    (pair.first != null && pair.second != null) || str.isBlank()
//                }
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//
//            StringSettingItem(
//                key = Key.FallbackToBreeno,
//                currentValue = state.value.fallbackToBreeno,
//                onChange = { str ->
//                    viewModel.sendIntent(
//                        OSIntent.SaveValue(
//                            Key.FallbackToBreeno,
//                            str.trim(' ', '\n')
//                        )
//                    )
//                }
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//            ToggleSettingItem(Key.EnableLaunchApp, state.value.enableLaunchApp, viewModel)
//            Spacer(modifier = Modifier.height(10.dp))
//            ToggleSettingItem(Key.EnableLaunchURI, state.value.enableLaunchUri, viewModel)
//            Spacer(modifier = Modifier.height(10.dp))
//            ToggleSettingItem(Key.EnableGetDeviceInfo, state.value.enableGetDeviceInfo, viewModel)
//            Spacer(modifier = Modifier.height(30.dp))
//
//            Text(
//                text = "这些工具的调用体验和模型智商有关~\n可能由于 API 协议差异导致调用出错",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
//                modifier = Modifier
//                    .padding(10.dp)
//                    .fillMaxWidth()
//            )
//        }
//    }
//}
//
//@Composable
//fun StringSettingItem(
//    key: Key, // 传入整个 OtherSettings 枚举项
//    currentValue: String = "", // 用于输入类型设置的当前字符串值
//    errorMsg: String = "",
//    onChange: (String) -> Unit = {}, // 用于输入类型，传入新的字符串值
//    validator: (String) -> Boolean = { true } // 新增：校验回调，返回 true 表示通过
//) {
//    // 状态应该由外部传入，这里只是管理 UI 自身的瞬态状态
//    var showInputDialog by remember { mutableStateOf(false) }
//    var inputText by remember { mutableStateOf(currentValue) } // 使用传入的 currentValue 初始化
//    var isError by remember { mutableStateOf(false) } // 新增：用于输入框的错误状态
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
////            .height(if (key.uiDescription?.isNotBlank() == true) 72.dp else 56.dp)
//            .clickable(onClick = {
//                // 点击时，将当前最新的值赋给 inputText，防止显示旧值
//                inputText = currentValue
//                isError = false // 每次打开对话框重置错误状态
//                showInputDialog = true
//            })
//            .padding(horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier.weight(0.7f)
//        ) {
//            // 标题
//            Text(
//                text = key.uiString,
//                style = MaterialTheme.typography.bodyLarge,
//            )
//            // 描述 - 可能没有
//            key.uiDescription.let {
//                if (!it.isNullOrBlank()) {
//                    Text(
//                        text = it,
//                        modifier = Modifier.padding(3.dp),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
//
//        // 预览
//        Text(
//            text = currentValue, // 显示外部传入的最新字符串值
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.primary,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            modifier = Modifier.weight(0.3f)
//        )
//    }
//
//    // 输入对话框
//    if (showInputDialog) {
//        AlertDialog(
//            onDismissRequest = { showInputDialog = false },
//            title = { Text(key.uiString) },
//            text = {
//                OutlinedTextField(
//                    value = inputText,
//                    onValueChange = {
//                        inputText = it
//                        isError = !validator(it) // 实时校验
//                    },
//                    singleLine = true,
//                    isError = isError, // 应用错误状态
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri), // 常用作 URL 或 IP
//                    modifier = Modifier.fillMaxWidth(),
//                    label = { Text(stringResource(string.input_value)) },
//                    supportingText = {
//                        if (isError) {
//                            Text(errorMsg) // 错误提示
//                        }
//                    }
//                )
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        if (!isError) { // 只有在没有错误时才确认
//                            onChange(inputText) // 传入新的值
//                            showInputDialog = false
//                        }
//                    },
//                    enabled = !isError // 按钮在有错误时禁用
//                ) {
//                    Text(stringResource(string.okay))
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showInputDialog = false }) {
//                    Text(stringResource(string.cancel))
//                }
//            },
//            modifier = Modifier.padding(20.dp)
//        )
//    }
//}
//
///**
// * 封装通用布尔设置项的 Composable。
// * 它负责从 ViewModel 的 UI 状态中获取当前值，并向 ViewModel 发送保存 Intent。
// *
// * @param key 设置项的唯一标识 Key。
// * @param viewModel OtherSettingsViewModel 实例，用于状态观察和发送 Intent。
// */
//@Composable
//fun ToggleSettingItem(
//    key: Key,
//    currentValue: Boolean,
//    viewModel: OtherSettingsViewModel
//) {
//    BooleanSettingItem(
//        key = key,
//        currentValue = currentValue,
//        onChange = { boolean ->
//            viewModel.sendIntent(OSIntent.SaveValue(key, boolean))
//        }
//    )
//}
//
//@Composable
//fun BooleanSettingItem(
//    key: Key, // 传入整个 OtherSettings 枚举项
//    currentValue: Boolean = false, // 用于开关类型设置的当前布尔值
//    onChange: (Boolean) -> Unit = {} // 用于开关类型，传入新的布尔值
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(if (key.uiDescription?.isNotBlank() == true) 72.dp else 56.dp)
//            .toggleable(
//                value = currentValue,
//                onValueChange = onChange,
//                role = Role.Switch
//            )
//            .padding(horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier.weight(1f)
//        ) {
//            Text(
//                text = key.uiString,
//                style = MaterialTheme.typography.bodyLarge,
//            )
//            key.uiDescription.let {
//                if (!it.isNullOrBlank()) {
//                    Text(
//                        text = it,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier.padding(3.dp),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
//
//        Switch(
//            checked = currentValue,
//            onCheckedChange = null // 因为 Row 已经处理了点击，这里设置为 null
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingTopBar(onBack: () -> Unit = {}) {
//    TopAppBar(
//        title = { Text(stringResource(string.other_settings_bar)) },
//        navigationIcon = {
//            IconButton(onClick = onBack) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // 使用 AutoMirrored.Filled.ArrowBack
//                    contentDescription = stringResource(string.back)
//                )
//            }
//        }
//    )
//}