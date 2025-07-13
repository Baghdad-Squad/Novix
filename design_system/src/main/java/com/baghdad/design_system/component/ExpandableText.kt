package com.baghdad.design_system.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 4
) {
    var expanded by remember { mutableStateOf(false) }
    val expandTag = stringResource(R.string.expand)

        val expandLabel = if (expanded) stringResource(R.string.read_less) else stringResource(R.string.read_more)

        val displayText = if (expanded) text else text.take(177).trimEnd()

        val annotatedText = buildAnnotatedString {
            append(displayText)
            pushStringAnnotation(tag = expandTag, annotation = expandTag)
            pushStyle(
                SpanStyle(
                    color = Theme.color.primary,
                    fontSize = Theme.typography.label.medium.fontSize
                )
            )
            append(expandLabel)
        }

        ClickableText(
            modifier = Modifier.padding(top = 4.dp),
            annotatedText = annotatedText,
            style = Theme.typography.body.small,
            color = Theme.color.body,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            onClick = { tag, _ ->
                if (tag == expandTag) {
                    expanded = !expanded
                }
            }
        )
    }


