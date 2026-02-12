package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews
import com.kxtdev.bukkydatasup.ui.theme.White


@Composable
fun PoshBucketBalance(
    modifier: Modifier = Modifier,
    header: String,
    value: String,
    isLoading: Boolean,
) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val allowedWidth = (maxWidth / 2) - 18.dp

    val shape = MaterialTheme.shapes.medium
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    val headerStyle = MaterialTheme.typography.labelMedium.copy(textColor)
    val valueStyle = MaterialTheme.typography.titleLarge.copy(textColor)

    PoshBucketBalanceShimmer(
        isLoading = isLoading,
        height = allowedWidth.times(0.65f),
        width = allowedWidth,
    ) {
        Box(
            modifier = modifier
                .width(allowedWidth)
                .padding(12.dp)
                .background(containerColor, shape),
            contentAlignment = Alignment.CenterStart,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = header,
                    style = headerStyle,
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = value,
                    style = valueStyle,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}


@Composable
fun PoshSingleRowSelectableSurface(
    label: String,
    onSelect: () -> Unit,
    isSelected: Boolean,
    enabled: Boolean,
    width: Dp = 200.dp,
) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val background: Color = if (isSelected) containerColor else Color.Transparent
    val shape = MaterialTheme.shapes.medium
    val textColor = MaterialTheme.colorScheme.onTertiary
    val textStyle = MaterialTheme.typography.titleSmall.copy(textColor)
    val mTextStyle: TextStyle = if(isSelected) textStyle.copy(color = White) else textStyle

    Box(
        Modifier
            .width(width)
            .padding(4.dp)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onSelect)
            .background(background, shape = shape)
            .border(if (isSelected) 1.5.dp else 1.dp, color = textStyle.color, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = mTextStyle,
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 12.dp,
                bottom = 12.dp
            )
        )
    }
}


@Composable
fun PoshDoubleRowSelectableSurface(
    topLabel: String,
    bottomLabel: String,
    onSelect: () -> Unit,
    isSelected: Boolean,
    enabled: Boolean,
    width: Dp = 200.dp,
    height: Dp = 350.dp,
    isLoading: Boolean
) {
    PoshPlanShimmer(isLoading = isLoading, itemWidth = width, itemHeight = height) {
        PoshDoubleRowSelectableSurface(
            topLabel = topLabel,
            bottomLabel = bottomLabel,
            onSelect = onSelect,
            isSelected = isSelected,
            enabled = enabled,
            width = width,
            height = height,
        )
    }
}

@Composable
private fun PoshDoubleRowSelectableSurface(
    topLabel: String,
    bottomLabel: String,
    onSelect: () -> Unit,
    isSelected: Boolean,
    enabled: Boolean,
    width: Dp = 200.dp,
    height: Dp = 350.dp,
){
    val selectedBorderColor = MaterialTheme.colorScheme.secondary
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val background: Color = if (isSelected) containerColor else Color.Transparent
    val textColor = MaterialTheme.colorScheme.onTertiary
    val shape = MaterialTheme.shapes.medium
    val overflow = TextOverflow.Ellipsis
    val topLabelStyle = MaterialTheme.typography.titleMedium.copy(textColor)
    val bottomLabelStyle = MaterialTheme.typography.labelLarge.copy(textColor)
    val mTopLabelStyle: TextStyle = if(isSelected) topLabelStyle.copy(color = White) else
        topLabelStyle
    val mBottomLabelStyle: TextStyle = if(isSelected) bottomLabelStyle.copy(color = White) else
        bottomLabelStyle


    Box(
        Modifier
            .width(width)
            .height(height)
            .padding(4.dp)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onSelect)
            .background(background, shape = shape)
            .border(
                if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) selectedBorderColor else containerColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            PoshAutoSizeText(
                text = topLabel,
                style = mTopLabelStyle,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                ),
                overflow = overflow,
                textAlign = TextAlign.Center,
                maxLines = 2,
                maxTextSize = mTopLabelStyle.fontSize
            )
            Spacer(Modifier.height(12.dp))
            PoshAutoSizeText(
                text = bottomLabel,
                style = mBottomLabelStyle,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                ),
                overflow = overflow,
                textAlign = TextAlign.Center,
                maxLines = 1,
                maxTextSize = mBottomLabelStyle.fontSize
            )
        }
    }
}

@Composable
fun PoshTripleRowSelectableSurface(
    topLabel: String,
    middleLabel: String,
    bottomLabel: String,
    onSelect: () -> Unit,
    isSelected: Boolean,
    enabled: Boolean,
    width: Dp = 200.dp,
    height: Dp = 350.dp,
    isLoading: Boolean,
) {
    PoshPlanShimmer(isLoading = isLoading, itemWidth = width, itemHeight = height) {
        PoshTripleRowSelectableSurface(
            topLabel = topLabel,
            middleLabel =middleLabel,
            bottomLabel = bottomLabel,
            onSelect = onSelect,
            isSelected = isSelected,
            enabled = enabled,
            width = width,
            height = height,
        )
    }
}


@Composable
private fun PoshTripleRowSelectableSurface(
    topLabel: String,
    middleLabel: String,
    bottomLabel: String,
    onSelect: () -> Unit,
    isSelected: Boolean,
    enabled: Boolean,
    width: Dp = 200.dp,
    height: Dp = 350.dp,
) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val selectedBorderColor = MaterialTheme.colorScheme.secondary
    val background: Color = if (isSelected) containerColor else Color.Transparent
    val textColor = MaterialTheme.colorScheme.onTertiary
    val shape = MaterialTheme.shapes.medium
    val overflow = TextOverflow.Ellipsis
    val topLabelStyle = MaterialTheme.typography.titleSmall.copy(textColor)
    val middleLabelStyle = MaterialTheme.typography.titleMedium.copy(textColor)
    val bottomLabelStyle = MaterialTheme.typography.labelLarge.copy(textColor)
    val mTopLabelStyle: TextStyle = if(isSelected) topLabelStyle.copy(White) else topLabelStyle
    val mMiddleLabelStyle: TextStyle = if(isSelected) middleLabelStyle.copy(White) else middleLabelStyle
    val mBottomLabelStyle: TextStyle = if(isSelected) bottomLabelStyle.copy(White) else bottomLabelStyle

    Box(
        Modifier
            .width(width)
            .height(height)
            .padding(4.dp)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onSelect)
            .background(background, shape = shape)
            .border(
                if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) selectedBorderColor else containerColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            PoshAutoSizeText(
                text = topLabel,
                style = mTopLabelStyle,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                ),
                overflow = overflow,
                textAlign = TextAlign.Center,
                maxLines = 2,
                maxTextSize = mTopLabelStyle.fontSize
            )
            Spacer(Modifier.height(12.dp))
            PoshAutoSizeText(
                text = middleLabel,
                style = mMiddleLabelStyle,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                ),
                overflow = overflow,
                textAlign = TextAlign.Center,
                maxLines = 1,
                maxTextSize = mMiddleLabelStyle.fontSize,
            )
            Spacer(Modifier.height(12.dp))
            PoshAutoSizeText(
                text = bottomLabel,
                style = mBottomLabelStyle,
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                ),
                maxLines = 1,
                overflow = overflow,
                textAlign = TextAlign.Center,
                maxTextSize = mBottomLabelStyle.fontSize
            )
        }
    }
}

@Composable
fun PoshSuggestionItem(
    modifier: Modifier = Modifier,
    icon: Int,
    header: String,
    subtitle: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val textColor = MaterialTheme.colorScheme.onTertiary
    val headerStyle = MaterialTheme.typography.titleMedium.copy(textColor)
    val subtitleStyle = MaterialTheme.typography.bodyMedium.copy(textColor)

    Box(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = headerStyle.color,
                modifier = Modifier
                    .size(38.dp)
                    .padding(4.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                Text(
                    text = header,
                    style = headerStyle,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = subtitle,
                    style = subtitleStyle,
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(id = PoshIcon.ArrowForward),
                contentDescription = null,
                tint = headerStyle.color,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun PoshRebrandedListItem(
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Dp = 72.dp,
) {

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                enabled = enabled,
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(height)
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 22.dp,
                    bottom = 22.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.weight(1f)) {
                title()
            }
            if (trailingIcon != null) {
                Box {
                    trailingIcon()
                }
            }
        }
    }

}

@Preview
@Composable
private fun PoshSingleRowSelectableSurfacePreview() {
    MainAppTheme {
        PoshSingleRowSelectableSurface(
            label = "N200",
            isSelected = true,
            onSelect = {},
            enabled = true,
            width = 200.dp
        )
    }
}

@Composable
@Preview
private fun PoshDoubleRowSelectableSurfacePreview() {
    MainAppTheme {
        PoshDoubleRowSelectableSurface(
            topLabel = "Jinja",
            bottomLabel = "N3,000",
            onSelect = {},
            isSelected = false,
            enabled = true,
            width = 200.dp,
            height = 350.dp,
        )
    }
}

@Composable
@Preview
private fun PoshTripleRowSelectableSurfacePreview() {
    MainAppTheme {
        PoshTripleRowSelectableSurface(
            topLabel = "1 Day",
            middleLabel = "200MB",
            bottomLabel = "N3,000",
            onSelect = {},
            isSelected = false,
            enabled = true,
            width = 200.dp,
            height = 350.dp,
        )
    }
}

@Composable
@Preview
private fun PoshSuggestionItemPreview() {
    MainAppTheme {
        PoshSuggestionItem(
            header = "Cable",
            subtitle = "6% discount",
            icon = PoshIcon.Network,
            onClick = {},
            enabled = true
        )
    }
}

@Composable 
@Preview 
private fun PoshBucketBalancePreview() {
    MainAppTheme {
        PoshBucketBalance(
            header = "MTN DATA BALANCE",
            value = "0.0GB",
            isLoading = false,
        )
    }
}

@Composable
@ThemePreviews
private fun PoshRebrandedListItemPreview() {
    MainAppTheme {
        PoshRebrandedListItem(
            enabled = true,
            onClick = {},
            trailingIcon = {},
            title = {},
        )

    }
}