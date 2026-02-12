package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshEmptyContent(
    message: String,
    icon: Int = PoshIcon.EmptyContent,
) {
    val textColor = MaterialTheme.colorScheme.onTertiary
    val headerStyle = MaterialTheme.typography.headlineMedium.copy(textColor)
    val textStyle = MaterialTheme.typography.titleMedium.copy(textColor)

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = icon),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    )
            )
            Spacer(Modifier.height(42.dp))
            Text(
                text = stringResource(id = R.string.no_records_found),
                style = headerStyle,
                textAlign = TextAlign.Center,
            )
            Text(
                text = message,
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
    }

}


@Composable
@Preview
private fun PoshEmptyContentPreview() {
    MainAppTheme {
        PoshEmptyContent(
            message = "No airtime.json records available",
        )
    }
}


