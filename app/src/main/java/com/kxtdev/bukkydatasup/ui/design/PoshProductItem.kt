package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshProductItem(
    product: Product,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val borderColor = if(enabled) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.primary

    val textColor = if(enabled) MaterialTheme.colorScheme.onPrimary else
        MaterialTheme.colorScheme.secondary

    val textStyle = MaterialTheme.typography.labelMedium.copy(textColor)

    val iconTint = if(enabled) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.secondary

    val shape = RoundedCornerShape(25.dp)

    val background = if(enabled) MaterialTheme.colorScheme.primary else
        MaterialTheme.colorScheme.primary.copy(0.2f)

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val allowedWidth: Dp = (maxWidth / 3) - 18.dp

    Box(
        Modifier
            .width(allowedWidth)
            .border(1.dp, borderColor, shape)
            .background(background, shape)
            .clip(shape)
            .clickable(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center) {
        Column(
            Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(product) {
                Product.WHATSAPP_GROUP -> {
                    Image(
                        painter = painterResource(id = product.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
                else -> {
                    Icon(
                        painter = painterResource(id = product.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = iconTint,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            PoshAutoSizeText(
                text = product.title,
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                maxTextSize = textStyle.fontSize,
                modifier = Modifier
                    .widthIn(max = allowedWidth - 16.dp)
            )
        }
    }
}


@Composable
@Preview
private fun ProductItemPreview() {
    MainAppTheme {
        PoshProductItem(
            product = Product.WHATSAPP_GROUP,
            onClick = {},
            enabled = true,
        )
    }
}