package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoshScrim() {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = TweenSpec(), label = "ScrimAlphaAnimation"
    )
    val color = BottomSheetDefaults.ScrimColor

    Canvas(
        Modifier
            .fillMaxSize()
    ) {
        drawRect(color = color, alpha = alpha)
    }
}
