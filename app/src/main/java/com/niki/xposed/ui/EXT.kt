package com.niki.xposed.ui


import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niki.common.Key


/**
 * [新增] 封装了通用样式的 OutlinedTextField，用于配置项输入。
 *
 * @param value 输入框的当前值。
 * @param onValueChange 值更改时的回调。
 * @param modifier 自定义修饰符。
 * @param label 输入框的标签文本。
 * @param description 正常的辅助/描述文本。
 * @param isError 是否处于错误状态。
 * @param errorText 当 isError 为 true 时显示的错误文本。
 * @param singleLine 是否为单行输入框。
 * @param visualTransformation 用于转换显示文本，如密码遮盖。
 * @param trailingIcon 输入框尾部的图标。
 * @param keyboardOptions 键盘选项，如设置键盘类型。
 * @param maxLines 最大行数。
 */
@Composable
fun CommonOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    label: String,
    description: String?,
    isError: Boolean = false,
    errorText: String? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = if (singleLine) 1 else 10
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        supportingText = {
            val text = if (isError) errorText else description
            text?.let {
                Text(it)
            }
        },
        isError = isError,
        modifier = modifier,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        maxLines = maxLines
    )
}

@Composable
fun ScalingButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    hPaddingRange: IntRange,
    vPaddingRange: IntRange,
    radiusRange: IntRange,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    roundButton: Boolean = true
) {
    fun Modifier.thenIf(
        condition: Boolean,
        modifier: Modifier.() -> Modifier
    ): Modifier {
        return if (condition) {
            this.then(modifier())
        } else this
    }

    val haptic = LocalHapticFeedback.current
    val shouldVibrate = remember { false }
    val isPressed by interactionSource.collectIsPressedAsState()

    val hPadding by animateIntAsState(
        targetValue = if (isPressed) hPaddingRange.start else hPaddingRange.endInclusive
    )
    val vPadding by animateIntAsState(
        targetValue = if (isPressed) vPaddingRange.start else vPaddingRange.endInclusive
    )
    val cornerRadius by animateIntAsState(
        targetValue = if (isPressed) radiusRange.start else radiusRange.endInclusive
    )

    Box(
        modifier = modifier
            .semantics { role = Role.Button }
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth + hPaddingRange.endInclusive.dp * 2,
                minHeight = ButtonDefaults.MinHeight * 1.2F + vPaddingRange.endInclusive.dp * 2
            )
            .padding(
                horizontal = hPadding.dp,
                vertical = vPadding.dp
            )
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = {
                    onClick()
                    if (shouldVibrate) haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                },
                onLongClick = {
                    onLongClick?.invoke()
                    if (shouldVibrate) haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                }
            )
            .thenIf(roundButton) {
                aspectRatio(1f)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
            style = textStyle
        )
    }
}

val composableContext: Context
    @Composable
    get() {
        return LocalContext.current
    }

val Int.resString: String
    @Composable
    get() {
        return stringResource(this)
    }

val Key.uiString: String
    @Composable
    get() {
        return stringResource(uiStringRes)
    }

val Key.uiDescription: String?
    @Composable
    get() {
        uiDescriptionRes?.let {
            return stringResource(it)
        }
        return null
    }

val titleTextStyle: TextStyle
    @Composable
    get() {
        return MaterialTheme.typography.bodyMedium.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    @StringRes titleId: Int,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(titleId)) },
        colors = topBarColors,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

val iconColors: IconButtonColors
    @Composable
    get() = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer // 图标颜色与 TopAppBar 的 Action Icon 颜色保持一致
    )

@OptIn(ExperimentalMaterial3Api::class)
val topBarColors: TopAppBarColors
    @Composable
    get() = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer, // AppBar 的背景色
        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer, // 标题颜色
        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // Action Icon 颜色
    )