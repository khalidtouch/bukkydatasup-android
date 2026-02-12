package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.common.models.AlertNotification
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews

@Composable
fun PoshAlertNotification(
    alertNotification: AlertNotification? = null
) {
    AnimatedVisibility(alertNotification != null && alertNotification.hasContent) {
        PoshAlertNotification(alertNotification?.alert.orEmpty())
    }
}

@Composable
private fun PoshAlertNotification(message: String) {
    val background = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    val textStyle = MaterialTheme.typography.labelMedium.copy(contentColor)
    val shape = MaterialTheme.shapes.medium
    val standardHeight = 72.dp

    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = standardHeight)
            .padding(
                start = 22.dp,
                end = 22.dp
            )
            .background(background,shape),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message,
            style = textStyle,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }
}



@Composable
@ThemePreviews
private fun PoshAlertNotificationPreviews() {
    MainAppTheme {
        PoshAlertNotification(
            AlertNotification("this is a new message\neveryday we move from place to place. as we may not need any financial support\nNot at all")
        )
    }
}