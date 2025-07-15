//package com.niki.breeno.ui
//
//import android.content.Context
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.Button
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.niki.breeno.R
//import com.niki.breeno.viewmodels.MainIntent
//import com.niki.breeno.viewmodels.MainViewModel
//import com.niki.common.Key
//import com.niki.common.MenuChoices
//import com.niki.common.toast
//
//
///**
// * 整个大模型参数配置界面的 Composable 函数
// */
//@Composable
//fun MainScreen(
//    viewModel: MainViewModel,
//    onMenuItemClicked: (MenuChoices) -> Unit = {}
//) {
//    val context = getContext()
//
//    val state = viewModel.uiState.collectAsStateWithLifecycle()
//
//    val baseUrl = state.value.baseUrl
//    val apiKey = state.value.apiKey
//    val modelName = state.value.modelName
//    val systemPrompt = state.value.systemPrompt
//    val timeout = state.value.timeout
//
//    val savedStr = stringResource(R.string.saved)
//    val saveFailedStr = stringResource(R.string.save_failed)
//
//    Scaffold(
//        topBar = { MainTopBar(onMenuItemClicked) }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState()) // 允许内容滚动
//                .fillMaxSize()
//        ) {
//            BaseUrlInput(baseUrl) {
//                viewModel.sendIntent(MainIntent.UpdateValue(Key.BaseUrl, it))
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            ApiKeyInput(apiKey) {
//                viewModel.sendIntent(MainIntent.UpdateValue(Key.ApiKey, it))
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            ModelNameInput(modelName) {
//                viewModel.sendIntent(MainIntent.UpdateValue(Key.ModelName, it))
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            TimeoutInput(timeout) {
//                viewModel.sendIntent(MainIntent.UpdateValue(Key.Timeout, it))
//            }
//            Spacer(modifier = Modifier.height(10.dp))
//            SystemPromptInput(systemPrompt) {
//                viewModel.sendIntent(MainIntent.UpdateValue(Key.SystemPrompt, it))
//            }
//            Spacer(modifier = Modifier.height(24.dp))
//            SaveButton {
//                try {
//                    viewModel.sendIntent(MainIntent.SaveValue(Key.ApiKey, apiKey))
//                    viewModel.sendIntent(MainIntent.SaveValue(Key.BaseUrl, baseUrl))
//                    viewModel.sendIntent(MainIntent.SaveValue(Key.ModelName, modelName))
//                    viewModel.sendIntent(MainIntent.SaveValue(Key.Timeout, timeout))
//                    viewModel.sendIntent(MainIntent.SaveValue(Key.SystemPrompt, systemPrompt))
//
//                    context.toast(savedStr)
//                } catch (t: Throwable) {
//                    context.toast("${saveFailedStr}: ${t.message}")
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainTopBar(onMenuItemClicked: (MenuChoices) -> Unit = {}) {
//    var showMenu by remember { mutableStateOf(false) } // 控制菜单显示隐藏
//    TopAppBar(
//        title = { Text(stringResource(R.string.app_bar)) },
//        actions = {
//            IconButton(onClick = { showMenu = true }) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = stringResource(R.string.more_options)
//                )
//            }
//
//            // 更多选项菜单
//            DropdownMenu(
//                expanded = showMenu,
//                onDismissRequest = { showMenu = false }
//            ) {
//                MenuChoices.entries.forEach { choice ->
//                    DropdownMenuItem(
//                        text = { Text(choice.uiString) },
//                        onClick = {
//                            showMenu = false
//                            onMenuItemClicked(choice)
//                        }
//                    )
//                }
//            }
//        }
//    )
//}
//
///**
// * 保存按钮 Composable
// */
//@Composable
//fun SaveButton(onSaveClick: () -> Unit) {
//    Button(
//        onClick = onSaveClick,
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        Text(
//            text = stringResource(R.string.save),
//            style = MaterialTheme.typography.bodyLarge,
//        )
//    }
//}
//
///**
// * Base URL 输入框 Composable
// */
//@Composable
//fun BaseUrlInput(
//    value: String,
//    onValueChange: (String) -> Unit
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(Key.BaseUrl.uiString) },
//        supportingText = {
//            Key.BaseUrl.uiDescription?.run {
//                Text(this)
//            }
//        },
//        modifier = Modifier.fillMaxWidth(),
//        singleLine = true
//    )
//}
//
///**
// * API Key 输入框 Composable，带隐藏字符功能
// */
//@Composable
//fun ApiKeyInput(
//    value: String,
//    onValueChange: (String) -> Unit
//) {
//    var passwordVisible by remember { mutableStateOf(false) }
//
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(Key.ApiKey.uiString) },
//        supportingText = {
//            Key.ApiKey.uiDescription?.run {
//                Text(this)
//            }
//        },
//        modifier = Modifier.fillMaxWidth(),
//        singleLine = true,
//        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//        trailingIcon = {
//            val image = if (passwordVisible) {
//                Icons.Filled.Visibility
//            } else {
//                Icons.Filled.VisibilityOff
//            }
//            val description =
//                if (passwordVisible) stringResource(R.string.hide_key) else stringResource(R.string.hide_key)
//
//            IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                Icon(imageVector = image, description)
//            }
//        }
//    )
//}
//
///**
// * 模型名称输入框 Composable
// */
//@Composable
//fun ModelNameInput(
//    value: String,
//    onValueChange: (String) -> Unit
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(Key.ModelName.uiString) },
//        modifier = Modifier.fillMaxWidth(),
//        singleLine = true,
//        supportingText = {
//            Key.ModelName.uiDescription?.run {
//                Text(this)
//            }
//        },
//    )
//}
//
///**
// * 超时 Composable
// */
//@Composable
//fun TimeoutInput(
//    value: Long,
//    onValueChange: (Long) -> Unit
//) {
//    // 将 Long 转换为 String 用于显示
//    var textState by remember { mutableStateOf(value.toString()) }
//
//    fun isError(test: String): Boolean {
//        val long = test.toLongOrNull()
//        return (test.isNotBlank() && (long == null || long < 0))
//    }
//
//    OutlinedTextField(
//        value = textState,
//        onValueChange = { newText ->
//            textState = newText
//            // 尝试将输入转换为 Long
//            val newValue = newText.toLongOrNull()
//            if (newValue != null) {
//                onValueChange(newValue)
//            } else {
//                // 如果输入无效，可选择不回调或回调默认值（如 0L）
//                onValueChange(0L)
//            }
//        },
//        label = { Text(Key.Timeout.uiString) },
//        modifier = Modifier
//            .fillMaxWidth(),
//        maxLines = 10,
//        isError = isError(textState), // 显示错误状态
//        supportingText = {
//            val str = if (isError(textState)) {
//                "请输入有效的数字"
//            } else {
//                Key.Timeout.uiDescription
//            }
//            str?.let {
//                Text(it)
//            }
//        }
//    )
//}
//
///**
// * 系统提示词输入框 Composable
// */
//@Composable
//fun SystemPromptInput(
//    value: String,
//    onValueChange: (String) -> Unit
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(Key.SystemPrompt.uiString) },
//        supportingText = {
//            Key.SystemPrompt.uiDescription?.run {
//                Text(this)
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 120.dp), // 设置最小高度，适应长文本
//        maxLines = 10 // 最大显示行数
//    )
//}
//
//@Composable
//fun getContext(): Context {
//    return LocalContext.current
//}