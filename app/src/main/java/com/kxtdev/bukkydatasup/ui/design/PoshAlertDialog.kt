package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews

val TAG = "PoshAlertDialog"

@Composable
fun PoshAlertDialog(
    itemsWithIcon: () -> List<Pair<String, Int>>,
    onDismiss: () -> Unit,
    selectedItemName: String?,
    onChangeItem: (itemName: String) -> Unit,
    title: String,
    visible: Boolean,
    paddingValues: PaddingValues,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
        ),
    ) {
        PoshAlertDialog(
            itemsWithIcon = itemsWithIcon.invoke(),
            onDismiss = onDismiss,
            selectedItemName =selectedItemName,
            onChangeItem = onChangeItem,
            title = title,
            paddingValues = paddingValues,
        )
    }
}


@Composable
private fun PoshAlertDialog(
    itemsWithIcon: List<Pair<String, Int>>,
    onDismiss: () -> Unit,
    selectedItemName: String?,
    onChangeItem: (itemName: String) -> Unit,
    title: String,
    paddingValues: PaddingValues,
) {
    val configuration = LocalConfiguration.current

    PoshScrim()
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 72.dp)
            .padding(paddingValues),
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onDismiss) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        },
        dismissButton = {
            TextButton(onDismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        },
        title = {
        },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PoshDialogPanel(
                    title = title,
                    itemsWithIcon = itemsWithIcon,
                    selectedItem = selectedItemName,
                    onChangeItem = onChangeItem,
                )
            }
        }
    )
}

@Composable
private fun PoshDialogPanel(
    title: String,
    itemsWithIcon: List<Pair<String, Int>>,
    selectedItem: String?,
    onChangeItem: (itemName: String) -> Unit
) {
    PoshDialogTitle(text = title)
    Column(Modifier.selectableGroup()) {
        itemsWithIcon.forEachIndexed { _, item ->
            PoshDialogThemeChooserRow(
                icon = item.second,
                name = item.first,
                selected = item.first.lowercase() == selectedItem?.lowercase(),
                onClick = { onChangeItem(item.first) },
            )
        }
    }
}

@Composable
private fun PoshDialogThemeChooserRow(
    icon: Int,
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.outline,
) {
    val radioColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedColor = MaterialTheme.colorScheme.outlineVariant,
    )

    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painterResource(id = icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(42.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(name.uppercase(), color = color)
        Spacer(Modifier.weight(1f))
        RadioButton(
            selected = selected,
            onClick = null,
            colors = radioColors,
        )
    }
}

@Composable
private fun PoshDialogTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.onTertiary
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            color = color,
        ),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}


@Composable
@ThemePreviews
private fun PoshAlertDialogPreviews() {
    MainAppTheme {
        PoshAlertDialog(
            itemsWithIcon = {
                Network.asPair(Product.DATA, listOf())
            },
            onDismiss = {},
            selectedItemName = Network.MTN.title,
            onChangeItem = {},
            title = "Title",
            visible = true,
            paddingValues = PaddingValues()
        )
    }
}