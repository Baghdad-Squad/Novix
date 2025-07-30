package com.baghdad.design_system.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun NovixTextField(
    value: String,
    keyBoardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    hint: String? = null,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    onClickTrailingIcon: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isTextMasked: Boolean = false,
    trailingVisibility: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Theme.color.primary else Theme.color.stroke,
        animationSpec = tween(durationMillis = 400)
    )

    val iconTint by animateColorAsState(
        targetValue = if (isFocused) Theme.color.primary else Theme.color.hint,
        animationSpec = tween(durationMillis = 400)
    )

    var internalValue by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (internalValue.text != value) {
            internalValue = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                style = Theme.typography.body.medium,
                color = Theme.color.body,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface, shape = RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        enabled = enabled && !readOnly,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { focusRequester.requestFocus() }
                    )
            ) {
                BasicTextField(
                    keyboardOptions = keyBoardOptions,
                    value = internalValue,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= maxLength) {
                            internalValue = newValue
                            onValueChange(newValue.text)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(12.dp),
                    enabled = enabled && !readOnly,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    visualTransformation = if (isTextMasked) PasswordVisualTransformation() else VisualTransformation.None,
                    textStyle = Theme.typography.body.medium.copy(color = Theme.color.body),
                    cursorBrush = SolidColor(Theme.color.primary),
                    interactionSource = interactionSource,
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            if (leadingIcon != null) {
                                Icon(
                                    painter = leadingIcon,
                                    contentDescription = stringResource(R.string.text_input_field),
                                    modifier = Modifier.size(24.dp),
                                    tint = iconTint
                                )
                            }

                            Box(Modifier.weight(1f)) {
                                if (value.isEmpty()) {
                                    hint?.let { HintTextField(it) }
                                }
                                innerTextField()
                            }
                                trailingIcon?.let {
                                    Icon(
                                        painter = it,
                                        contentDescription = if (trailingVisibility) stringResource(
                                            R.string.hide
                                        ) else stringResource(R.string.show),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple(
                                                    bounded = true,
                                                    radius = 100.dp,

                                                    )
                                            ) {

                                                onClickTrailingIcon?.invoke()
                                            },
                                        tint = iconTint
                                    )
                                }
                            }

                    }
                )
            }
        }
    }
}


@Composable
private fun HintTextField(placeholder: String) {
    Text(
        text = placeholder,
        style = Theme.typography.body.small,
        color = Theme.color.hint
    )
}


@Preview
@Composable
fun NovixTextFieldPreview() {
    NovixTextField(
        value = "",
        onValueChange = {},
        label = "Title",
        hint = "value",
        leadingIcon = painterResource(R.drawable.ic_user)
    )
}

@Preview
@Composable
fun NovixTextFieldPreview2() {
    NovixTheme(isDarkTheme = true) {
        NovixTextField(
            value = "Value",
            onValueChange = {},
            label = "Title",
            hint = "value",
            leadingIcon = painterResource(R.drawable.ic_user)
        )
    }

}
