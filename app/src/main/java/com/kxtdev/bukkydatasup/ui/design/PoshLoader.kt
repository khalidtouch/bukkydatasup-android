package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshLoader(
    modifier: Modifier = Modifier,
    loadingMessage: String? = null
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val trackColor = containerColor.copy(0.2f)
    val imageSize = 48.dp
    val messageStyle = MaterialTheme.typography.titleSmall.copy(
        MaterialTheme.colorScheme.onTertiary
    )

    PoshScrim()
    Column(
        Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier, contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = PoshIcon.Logo),
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape),
            )
            CircularProgressIndicator(
                modifier = modifier.size(imageSize.plus(12.dp)),
                color = containerColor,
                trackColor = trackColor,
                strokeCap = StrokeCap.Round,
                strokeWidth = 4.dp,
            )
        }
        loadingMessage?.let { msg ->
            Spacer(Modifier.height(12.dp))
            Text(
                text = msg,
                style = messageStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PoshLinearLoader(
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val trackColor = containerColor.copy(0.2f)

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = containerColor,
            trackColor = trackColor,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun PoshInlineLoader(
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val trackColor = containerColor.copy(0.2f)
    CircularProgressIndicator(
        modifier = modifier.size(18.dp),
        color = containerColor,
        trackColor = trackColor,
        strokeCap = StrokeCap.Round,
        strokeWidth = 2.dp,
    )
}

@Composable
@Preview
private fun PoshLoaderPreview() {
    MainAppTheme {
        PoshLoader(loadingMessage = "Loading...")
    }
}

@Composable
@Preview
private fun PoshLinearLoaderPreview() {
    MainAppTheme {
        PoshLinearLoader(

        )
    }
}

@Composable
@Preview
private fun PoshInlineLoaderPreview() {
    MainAppTheme {
        PoshInlineLoader()
    }
}