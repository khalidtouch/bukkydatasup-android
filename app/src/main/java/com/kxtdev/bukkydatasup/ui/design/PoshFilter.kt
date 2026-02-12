package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.Transparent

@Composable
fun PoshFilter(
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean,
    selectedItem: String?,
) {
    val selectedShapeDp = 24.dp
    val unselectedShapeDp = 24.dp
    val unselectedContainerColor = MaterialTheme.colorScheme.secondary
    val selectedContainerColor = MaterialTheme.colorScheme.secondary
    val unselectedContentColor = MaterialTheme.colorScheme.onSecondary
    val selectedContentColor = MaterialTheme.colorScheme.onSecondary
    val contentColor = if(isSelected) selectedContentColor else unselectedContentColor
    val containerColor = if(isSelected) selectedContainerColor else unselectedContainerColor
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = contentColor,
        fontWeight = FontWeight.Bold,
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val minWidth = (screenWidth / 2) - 24.dp

    val animatedShapeDps by animateDpAsState(
        targetValue = if(isSelected) selectedShapeDp else unselectedShapeDp, 
        label = "ShapeAnimation"
    )
    
    val shape = RoundedCornerShape(animatedShapeDps)
    val icon = if(isSelected) PoshIcon.ArrowUp else PoshIcon.ArrowDown
    
    Box(
        modifier = Modifier
            .background(containerColor, shape)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { onToggle.invoke(isSelected) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .widthIn(min = minWidth),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(12.dp))
            PoshAutoSizeText(
                text = selectedItem ?: stringResource(id = R.string.summary),
                style = textStyle,
                textAlign = TextAlign.Center,
                maxTextSize = textStyle.fontSize,
                maxLines = 1
            )
            Spacer(Modifier.width(12.dp))
            Icon(
               painterResource(id = icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


@Composable
fun PoshFilterOptions(
    isVisible: Boolean,
    selectedItem: String?,
    onSelect: (String?) -> Unit,
    enabled: Boolean,
    options: List<String>,
    topPadding: Dp,
) {
    AnimatedVisibility(visible = isVisible) {
        Box(Modifier.fillMaxSize()
            .padding(
                top = topPadding,
                start = 4.dp,
                end = 4.dp,
                bottom = 4.dp,
            ), contentAlignment = Alignment.TopCenter) {
            PoshFilterOptions(
                selectedItem = selectedItem,
                onSelect = onSelect,
                enabled = enabled,
                options = options,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PoshFilterOptions(
    selectedItem: String?,
    onSelect: (String?) -> Unit,
    enabled: Boolean,
    options: List<String>
) {
    val containerColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.secondary
    val shape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 14.dp,
        bottomEnd = 14.dp,
    )

    Box(
        Modifier
            .fillMaxWidth()
            .background(containerColor, shape)
            .border(1.dp, borderColor, shape),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    options.forEachIndexed { _, option ->
                        val selected = option == selectedItem
                        PoshSelectableChip(
                            label = option,
                            isSelected = selected,
                            onToggle = { onSelect.invoke(option) },
                            enabled = enabled,
                        )

                        Spacer(Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}


@Composable
@Preview
private fun PoshFilterPreview() {
   MainAppTheme {
       PoshFilter(
           isSelected = true,
           onToggle = {},
           enabled = true,
           selectedItem = null,
       )
   }
}

@Composable
@Preview
private fun PoshFilterPreviewUnselected() {
    MainAppTheme {
        PoshFilter(
            isSelected = false,
            onToggle = {},
            enabled = true,
            selectedItem = null,
        )
    }
}

@Composable
@Preview
private fun PoshFilterOptionsPreview() {
    MainAppTheme {
        PoshFilterOptions(
            isVisible = true,
            selectedItem = null,
            onSelect = {},
            enabled = true,
            options = listOf("Main", "Boy", "Fridge"),
            topPadding = 18.dp
        )
    }
}