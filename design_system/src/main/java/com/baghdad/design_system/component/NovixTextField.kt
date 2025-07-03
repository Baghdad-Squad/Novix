package com.baghdad.design_system.component

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun NovixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    placeholder: String = "",
    @DrawableRes leadingIcon: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
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

    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
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
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(12.dp),
                    enabled = enabled && !readOnly,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
                                    painter = painterResource(leadingIcon),
                                    contentDescription = "text input field",
                                    modifier = Modifier.size(24.dp),
                                    tint = iconTint
                                )
                            }

                            Box(Modifier.weight(1f)) {
                                if (value.isEmpty()) {
                                    PlaceholderTextField(placeholder)
                                }
                                innerTextField()
                            }
                            if (isPassword) {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible }
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            if (passwordVisible) R.drawable.eye
                                            else R.drawable.ic_heroicons_outline
                                        ),
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        modifier = Modifier.size(24.dp),
                                        tint = iconTint
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}




@Composable
private fun PlaceholderTextField(placeholder: String) {
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
        title = "Title",
        placeholder = "value",
        leadingIcon = R.drawable.user
    )
}
@Preview
@Composable
fun NovixTextFieldPreview2() {
    NovixTextField(
        value = "Value",
        onValueChange = {},
        title = "Title",
        placeholder = "value",
        leadingIcon = R.drawable.user
    )
}

