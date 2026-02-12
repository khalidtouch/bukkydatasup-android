package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun PoshBucketBalanceShimmer(
    isLoading: Boolean,
    height: Dp,
    width: Dp,
    content: @Composable () -> Unit,
) {
    val shape = MaterialTheme.shapes.medium

    if(isLoading) {
        Box(
            Modifier
                .width(width)
                .height(height)
                .padding(12.dp)
                .clip(shape)
                .shimmerEffect()
        )
    } else content.invoke()
}

@Composable
fun PoshWalletCardShimmer(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    val shape = MaterialTheme.shapes.medium

    if(isLoading) {
        Box(
            Modifier
                .width(300.dp)
                .height(180.dp)
                .padding(4.dp)
                .clip(shape)
                .shimmerEffect()
        )
    } else content.invoke()
}

@Composable
fun PoshHistoryShimmer(
    isLoading: Boolean,
    itemWidth: Dp,
    itemHeight: Dp,
    contentAfterLoading: @Composable () -> Unit
) {
    if(isLoading) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                Modifier
                    .width(itemWidth)
                    .height(itemHeight)
                    .padding(4.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )
        }
    } else contentAfterLoading.invoke()
}

@Composable
fun PoshPlanShimmer(
    isLoading: Boolean,
    itemWidth: Dp,
    itemHeight: Dp,
    contentAfterLoading: @Composable () -> Unit
) {
    if(isLoading) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                Modifier
                    .width(itemWidth)
                    .height(itemHeight)
                    .padding(4.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )
        }

    } else contentAfterLoading.invoke()
}



fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmerAnimation")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
        ), label = "shimmerAnimation"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5).copy(0.2f),
                Color(0xFF8F8B8B).copy(0.3f),
                Color(0xFFB8B5B5).copy(0.2f),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}
