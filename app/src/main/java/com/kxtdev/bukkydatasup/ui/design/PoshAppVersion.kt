package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.BuildConfig
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews

@Composable
fun PoshAppVersion() {
    val version = BuildConfig.VERSION_NAME
    val color = MaterialTheme.colorScheme.onSurface
    val style = MaterialTheme.typography.labelSmall.copy(color)
    Text(
        text = "Version $version",
        style = style,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
            )
    )
}


@Composable
@ThemePreviews
private fun PoshAppVersionPreview() {
    MainAppTheme {
        PoshAppVersion()
    }
}