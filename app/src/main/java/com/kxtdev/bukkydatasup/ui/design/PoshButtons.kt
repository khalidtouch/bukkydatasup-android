package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.Black
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews
import com.kxtdev.bukkydatasup.ui.theme.White

enum class ButtonRole {
    PRIMARY, SECONDARY, VARIANT
}

@Composable
fun PoshButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    role: ButtonRole,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 4.dp,
    textStyle: TextStyle =  MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
    onClick: () -> Unit,
) {
    val containerColor: Color = when(role) {
        ButtonRole.SECONDARY -> Color.Transparent
        ButtonRole.VARIANT -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor: Color = when(role) {
        ButtonRole.SECONDARY -> MaterialTheme.colorScheme.primaryContainer
        ButtonRole.VARIANT -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    val shape = CircleShape

    val colors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    val buttonModifier: Modifier = if(role == ButtonRole.SECONDARY) {
        modifier
            .border(1.dp, contentColor, shape = shape)
    } else modifier

    Button(
        modifier = buttonModifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                start = horizontalPadding,
                end = horizontalPadding,
                top = verticalPadding,
                bottom = verticalPadding
            )
        )
    }

}

@Composable
fun PoshFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val shape = MaterialTheme.shapes.large
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

    FloatingActionButton(
        onClick = onClick,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp
        ),
        modifier = modifier,
        content = content
    )
}

@Composable
fun PoshTextActionButton(
    modifier: Modifier = Modifier,
    label: String,
    contentColor: Color,
    containerColor: Color,
    onClick: () -> Unit,
) {
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        contentColor, fontWeight = FontWeight.Bold
    )
    val shape = MaterialTheme.shapes.extraSmall

    Box(
        modifier = modifier
            .background(containerColor, shape)
            .clip(shape)
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier.padding(8.dp)
        )
    }
}



@Composable
fun PoshTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val textStyle = MaterialTheme.typography.labelLarge
        .copy(contentColor)

    val colors = ButtonDefaults.textButtonColors(
        containerColor = if(enabled) containerColor else Color.Transparent,
        contentColor = if(enabled) textStyle.color else Color.Transparent
    )

    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}



@Composable
@ThemePreviews
private fun PoshFloatingActionButtonPreview() {
    MainAppTheme {
        PoshFloatingActionButton(
            onClick = {},
            content = {
                Icon(
                   painterResource(id = PoshIcon.WhatsApp),
                  contentDescription = null,
                )
            }
        )
    }
}


@Composable
@ThemePreviews
private fun PoshButtonPreview() {
    MainAppTheme {
        PoshButton(
            text = "Click Here!",
            enabled = true,
            onClick = {},
            role = ButtonRole.PRIMARY,
        )
    }
}

@Composable
@ThemePreviews
private fun PoshButtonVariantPreview() {
    MainAppTheme {
        PoshButton(
            text = "Click Variant!",
            enabled = true,
            onClick = {},
            role = ButtonRole.VARIANT,
        )
    }
}


@Composable
@ThemePreviews
private fun PoshButtonSecondaryPreview() {
    MainAppTheme {
        PoshButton(
            text = "Click Secondary!",
            enabled = true,
            onClick = {},
            role = ButtonRole.SECONDARY,
        )
    }
}


@Composable
@ThemePreviews
private fun PoshTextActionButtonPreview() {
    MainAppTheme {
        PoshTextActionButton(
            label = "Click Here",
            contentColor = White,
            containerColor = Black,
            onClick = {}
        )
    }
}



