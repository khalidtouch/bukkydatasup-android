package com.kxtdev.bukkydatasup.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme

@Composable
fun PoshToggleSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.primary,
        checkedTrackColor = MaterialTheme.colorScheme.secondary,
        checkedBorderColor = MaterialTheme.colorScheme.surface,
        checkedIconColor = MaterialTheme.colorScheme.primary,
    uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
    uncheckedTrackColor = MaterialTheme.colorScheme.surface,
    uncheckedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    uncheckedIconColor = Color.Transparent,
    ),
    thumbContent: (@Composable () -> Unit)? = null,
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = colors,
        thumbContent = thumbContent,
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PoshSwitch(
    selectedOption: String,
    onSelect: (String) -> Unit,
    options: List<String>,
    enabled: Boolean,
) {
    val spec: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
        expandIn(
            initialSize = { it },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
        ) with shrinkOut(
            targetSize = { it },
            animationSpec = tween(350, easing = FastOutSlowInEasing)
        )
    }

    AnimatedContent(
        targetState = selectedOption,
        label = "SwitchAnimation",
        transitionSpec = spec,
    ) { option ->
        PoshSwitch(
            modifier = Modifier,
            selectedOption = option,
            onSelect = onSelect,
            options = options,
            enabled = enabled,
        )
    }
}

@Composable
private fun PoshSwitch(
    modifier: Modifier = Modifier,
    selectedOption: String,
    onSelect: (String) -> Unit,
    options: List<String>,
    enabled: Boolean,
) {
    val bend = 12.dp
    val startShape = RoundedCornerShape(
        topStart = bend,
        topEnd = 0.dp,
        bottomStart = bend,
        bottomEnd = 0.dp,
    )
    val centerShape = RectangleShape
    val endShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = bend,
        bottomStart = 0.dp,
        bottomEnd = bend,
    )
    val textStyle = MaterialTheme.typography.titleSmall

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            options.forEachIndexed { index, s ->
                val selected = s.lowercase() == selectedOption.lowercase()
                val containerColor = if(selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                val borderColor = if(selected) Color.Transparent else MaterialTheme.colorScheme.primaryContainer

                val shape = when(index) {
                    0 -> startShape
                    options.size - 1 -> endShape
                    else -> centerShape
                }

                val textColor = if(selected) MaterialTheme.colorScheme.onPrimaryContainer else
                    MaterialTheme.colorScheme.primaryContainer

                Box(
                    Modifier
                        .background(containerColor, shape = shape)
                        .border(1.dp, borderColor, shape)
                        .clip(shape)
                        .clickable(enabled = enabled, onClick = { onSelect.invoke(s) }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = s,
                        style = textStyle.copy(textColor),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(Modifier.width(4.dp))
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview
private fun PoshSwitchPreview() {
    MainAppTheme {
        PoshSwitch(
            selectedOption = "Man",
            onSelect = {},
            options = listOf("Man", "Mango", "Apple"),
            enabled = true,
        )
    }
}


@Composable
@Preview
private fun PoshToggleSwitchPreview() {
    MainAppTheme {

        Column {
            PoshToggleSwitch(
                modifier = Modifier,
                checked = true,
                enabled = true,
                onCheckedChange = {},
            )

            PoshToggleSwitch(
                modifier = Modifier,
                checked = true,
                enabled = false,
                onCheckedChange = {},
            )

            PoshToggleSwitch(
                modifier = Modifier,
                checked = false,
                enabled = true,
                onCheckedChange = {},
            )

            PoshToggleSwitch(
                modifier = Modifier,
                checked = false,
                enabled = false,
                onCheckedChange = {},
            )
        }


    }
}
