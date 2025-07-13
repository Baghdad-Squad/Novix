package com.baghdad.design_system.component


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
@Composable
fun ClickableText(
    annotatedText: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClick:(String, String) -> Unit
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val clickableModifier = Modifier.pointerInput(annotatedText) {
        detectTapGestures { position ->
            layoutResult?.let { result ->
                val offset = result.getOffsetForPosition(position)
                val annotations = annotatedText.getStringAnnotations(start = offset, end = offset)
                annotations.firstOrNull()?.let { annotation ->
                    onClick(annotation.tag, annotation.item)
                }
            }
        }
    }

    BasicText(
        text = annotatedText,
        modifier = modifier.then(clickableModifier),
        style = style.merge(TextStyle(color = color)),
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = {
            layoutResult = it
            onTextLayout(it)
        }
    )
}
