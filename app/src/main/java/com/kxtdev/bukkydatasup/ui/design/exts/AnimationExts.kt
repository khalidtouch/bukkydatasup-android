package com.kxtdev.bukkydatasup.ui.design.exts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kxtdev.bukkydatasup.common.utils.Settings


@Composable
fun PoshBox(
    modifier: Modifier,
    isVisible: Boolean = true,
    contentAlignment: Alignment,
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing)),
        exit = fadeOut(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = modifier,
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}

@Composable
fun PoshColumn(
    modifier: Modifier,
    isVisible: Boolean = true,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing)),
        exit = fadeOut(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing))
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

@Composable
fun PoshRow(
    modifier: Modifier,
    isVisible: Boolean = true,
    verticalAlignment: Alignment.Vertical,
    horizontalArrangement: Arrangement.Horizontal,
    content: @Composable RowScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing)),
        exit = fadeOut(tween(Settings.ANIMATION_DURATION_MILLIS, easing = FastOutSlowInEasing))
    ) {
        Row(
           modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content,
        )
    }
}