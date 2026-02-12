package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun PoshScaffold(
    modifier: Modifier = Modifier,
    toolbar: @Composable (() -> Unit)? = null,
    bottomNav: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                toolbar?.invoke()
            },
            bottomBar = {
                bottomNav?.invoke()
            },
            content = { innerPadding ->
                content.invoke(innerPadding)
            },
            containerColor = Transparent
        )

        Box(
            Modifier.fillMaxSize()
                .padding(
                    end = 12.dp,
                    bottom = 160.dp
                ),
            contentAlignment = Alignment.BottomEnd
        ) {
            floatingActionButton?.invoke()
        }
    }

}

@Composable
@Preview
private fun PoshScaffoldPreview() {
    PoshScaffold(
        toolbar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Black)
            ) {
                Text("Welcome to Posh Designs", style = MaterialTheme.typography.displaySmall)
            }
        },
        bottomNav = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Black)
            ) {
                Text("Bottom Navigation", style = MaterialTheme.typography.displaySmall)
            }
        },
        floatingActionButton = {
            Box(
                Modifier
                    .size(44.dp)
                    .border(1.dp, Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text("Click here")
            }
        },
        content = {
            Box(Modifier.fillMaxSize()) {
                Text("Main Content")
            }
        }
    )
}