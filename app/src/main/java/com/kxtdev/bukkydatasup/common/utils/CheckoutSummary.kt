package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.CheckoutSummary(
    params: HashMap<String, String>
) {
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    val maxKeyWidth = (maxWidth / 2) - 42.dp
    val maxValueWidth = (maxWidth / 2) - 48.dp
    val textColor = MaterialTheme.colorScheme.onTertiary
    val textStyle = MaterialTheme.typography.titleSmall.copy(textColor)

    params.map { param ->
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = param.key,
                style = textStyle.copy(fontWeight = FontWeight.ExtraBold),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                textAlign = TextAlign.Start,
                modifier = Modifier.widthIn(max = maxKeyWidth)
            )
            Spacer(Modifier.width(12.dp))
            Spacer(Modifier.weight(1f))
            Text(
                text = param.value,
                style = textStyle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 10,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(max = maxValueWidth)
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}