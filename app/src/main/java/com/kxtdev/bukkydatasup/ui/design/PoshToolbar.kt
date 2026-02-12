package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun PoshToolbarLarge(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    navigation: @Composable (() -> Unit)? = null
) {
    var titleVerticalOffset by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        Modifier
            .fillMaxWidth()
            .then(modifier), contentAlignment = Alignment.TopCenter) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = titleVerticalOffset.plus(0.dp)),
            contentAlignment = Alignment.BottomCenter) {
            title.invoke()
        }
        Row(
            Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    with(density) {
                        titleVerticalOffset = it.size.height.toDp()
                    }
                }
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigation?.let { nav ->
                Box(Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
                    nav.invoke()
                }
            }
            Spacer(Modifier.weight(1f))
            actions?.invoke(this)
        }
    }
}

@Composable
fun PoshToolbar(
    modifier: Modifier = Modifier,
    background: Color = Transparent,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    navigation: @Composable (() -> Unit)? = null
) {
    var allowedWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        Modifier
            .fillMaxWidth()
            .background(background)
            .padding(top = 8.dp, bottom = 8.dp)
            .then(modifier), contentAlignment = Alignment.CenterStart) {
        Box(
            Modifier
                .width(allowedWidth)
                .padding(
                    start = 8.dp,
                    end = 8.dp
                ), contentAlignment = Alignment.Center
        ) {
            title.invoke()
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            navigation?.let { nav ->
                Box(Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
                    nav.invoke()
                }
            }
            Spacer(
                Modifier
                    .weight(1f)
                    .onGloballyPositioned { with(density) { allowedWidth = it.size.width.toDp() } }
            )
            actions?.invoke(this)
        }
    }
}

@Composable
@Preview
private fun PoshToolbarLargePreviews() {
    MainAppTheme {
        PoshToolbarLarge(title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("header")
                Text("subtitle")
            }
        },
            actions = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            },
            navigation = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
@Preview
private fun PoshToolbarPreview() {
    MainAppTheme {
        PoshToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text("Title")
            },
            actions = {
                Text("Action 1")
                Text("Action 2")
            },
            navigation = {
                Text("Nav")
            }
        )
    }
}