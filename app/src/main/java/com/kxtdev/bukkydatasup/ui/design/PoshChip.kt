package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshIconChip(
    modifier: Modifier,
    enabled: Boolean,
    borderColor: Color,
    label: String,
    icon: Int,
    onClick: () -> Unit,
) {
    val labelStyle = MaterialTheme.typography.bodyLarge.copy(
        color = borderColor,
        fontWeight = FontWeight.SemiBold
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                style = labelStyle,
            )
            Icon(
                painterResource(id = icon),
                contentDescription = null,
                tint = borderColor,
            )
        }
    }
}


@Composable
fun PoshSelectableChip(
    label: String,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean,
) {
    val shapeDp = 12.dp
    val shape = RoundedCornerShape(shapeDp)
    val unselectedContainerColor = MaterialTheme.colorScheme.secondary
    val selectedContainerColor = MaterialTheme.colorScheme.primary
    val unselectedContentColor = MaterialTheme.colorScheme.onSecondary
    val selectedContentColor = MaterialTheme.colorScheme.onPrimary
    val contentColor = if(isSelected) selectedContentColor else unselectedContentColor
    val containerColor = if(isSelected) selectedContainerColor else unselectedContainerColor
    val textStyle = MaterialTheme.typography.titleSmall.copy(contentColor)
    val borderColor = if(isSelected) Color.Transparent else MaterialTheme.colorScheme.onTertiary

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp
    val maxTextWidth = (maxWidth / 2) - 72.dp

    Box(
        Modifier
            .background(containerColor, shape)
            .border(1.dp,borderColor,shape)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { onToggle.invoke(isSelected) }
            ),
        contentAlignment = Alignment.Center
    ) {
        PoshAutoSizeText(
            text = label,
            style = textStyle,
            maxTextSize = textStyle.fontSize,
            maxLines = 1,
            modifier = Modifier
                .widthIn(max = maxTextWidth)
                .padding(12.dp)
        )
    }
}


@Composable
@Preview
private fun PoshSelectableChipPreview() {
    MainAppTheme {
        PoshSelectableChip(
            label = "Airtime",
            isSelected = true,
            onToggle = {},
            enabled = true
        )
    }
}

@Composable
@Preview
private fun PoshSelectableChipPreviewUnselected() {
    MainAppTheme {
        PoshSelectableChip(
            label = "Airtime",
            isSelected = false,
            onToggle = {},
            enabled = true,
        )
    }
}