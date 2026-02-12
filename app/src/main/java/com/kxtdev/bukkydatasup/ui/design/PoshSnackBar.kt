package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.design.exts.PoshColumn
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme

@Composable
fun PoshSnackBar(
    message: String,
    withOffset: Boolean = false,
    onAction: () -> Unit,
    label: String = stringResource(id = R.string.dismiss),
    paddingValues: PaddingValues,
) {
    val containerColor = MaterialTheme.colorScheme.inverseSurface
    val contentColor = MaterialTheme.colorScheme.inverseOnSurface
    val messageStyle = MaterialTheme.typography.bodyLarge.copy(contentColor)
    val density = LocalDensity.current
    var maxWidth by remember { mutableStateOf(0.dp) }
    val bottomOffset = if(withOffset) 132.dp else 72.dp

    PoshScrim()
    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = bottomOffset, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        PoshColumn(
            Modifier
                .fillMaxWidth()
                .onGloballyPositioned { with(density) { maxWidth = it.size.width.toDp() } }
                .background(containerColor, MaterialTheme.shapes.small),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message,
                style = messageStyle,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 8.dp, top = 12.dp)
                    .widthIn(
                        max = maxWidth
                            .times(5)
                            .div(7)
                    ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(Modifier.height(8.dp))
            PoshTextActionButton(
                modifier = Modifier.padding(8.dp),
                label = label,
                contentColor = contentColor,
                containerColor = containerColor.copy(0.2f),
                onClick = onAction,
            )
        }
    }
}


@Composable
@Preview
private fun PoshSnackBarPreview() {
    MainAppTheme {
        PoshSnackBar(
            message = "the message",
            onAction = {},
            paddingValues = PaddingValues()
        )
    }
}