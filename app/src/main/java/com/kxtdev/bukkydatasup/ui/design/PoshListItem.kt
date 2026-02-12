package com.kxtdev.bukkydatasup.ui.design

import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun PoshServiceListItem(
    modifier: Modifier = Modifier,
    product: Product,
    enabled: Boolean,
    onClick: (Product) -> Unit,
) {
    val shape = RoundedCornerShape(24.dp)
    val containerColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onPrimary
    val textStyle = MaterialTheme.typography.titleMedium.copy(textColor)

    Box(
        modifier
            .clip(shape)
            .background(containerColor, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = { onClick.invoke(product) },
            )
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 18.dp,
                    bottom = 18.dp,
                    start = 12.dp,
                    end = 18.dp,
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = product.icon),
                contentDescription = null,
                tint = textColor
            )
            Spacer(Modifier.width(12.dp))
            PoshAutoSizeText(
                text = product.title,
                style = textStyle,
                maxTextSize = textStyle.fontSize,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
@ThemePreviews
private fun PoshListItemPreviews() {
    MainAppTheme {
        PoshServiceListItem(
            product = Product.CABLE,
            enabled = true,
            onClick = {}
        )
    }
}