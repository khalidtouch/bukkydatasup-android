package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshCircularTabGroup(
    modifier: Modifier = Modifier,
    count: Int = 3,
    selectedIndex: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            PoshCircularTab(
                modifier = modifier,
                isSelected = index == selectedIndex,
            )
            Spacer(Modifier.width(12.dp))
        }
    }
}

@Composable
fun PoshCircularTab(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    size: Dp = 12.dp,
) {
    val selectedColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
    val unselectedColor: Color = selectedColor.copy(0.2f)
    val background by animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor, tween(
            200,
            easing = FastOutSlowInEasing
        ), label = "TabColorAnim"
    )
    Box(
        modifier = modifier
            .background(background, CircleShape)
            .size(size)
            .clip(CircleShape)
    )
}


@Composable
@Preview
private fun PoshCircularTabPreview() {
    MainAppTheme {
        PoshCircularTab(
            isSelected = true,
        )
    }
}


@Composable
@Preview
private fun PoshCircularTabGroupPreview() {
    MainAppTheme {
        PoshCircularTabGroup(
            count = 3,
            selectedIndex = 1
        )
    }
}