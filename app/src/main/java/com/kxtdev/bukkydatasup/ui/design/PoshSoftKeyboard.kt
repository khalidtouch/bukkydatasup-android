package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.Transparent

val DEFAULT_BUTTON_WIDTH = 86.dp
val DEFAULT_BUTTON_HEIGHT = 52.dp
val DEFAULT_ICON_SIZE = 22.dp
val DEFAULT_KEYPAD_ROW_VERTICAL_SEPARATOR = 4.dp
val DEFAULT_KEYPAD_ROW_HORIZONTAL_SEPARATOR = 4.dp
const val MAX_ALLOWED_PIN_LENGTH = 5

@Composable
fun PoshSoftKeyboard(
    currentInputText: String,
    enabled: Boolean,
    onClick: (String?) -> Unit,
    useFingerprint: Boolean = true,
    label: String? = null,
    paddingValues: PaddingValues,
) {
    val buttonKeys: Array<Array<String>> = arrayOf(
        arrayOf("1","2","3"),
        arrayOf("4","5","6"),
        arrayOf("7","8","9"),
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(Modifier.height(102.dp))

        Image(
            painterResource(id = PoshIcon.Logo),
            contentDescription = null,
            modifier = Modifier.size(102.dp),
        )

        Spacer(Modifier.weight(1f))

        label?.let { l ->
            val labelStyle = MaterialTheme.typography.titleMedium.copy(
                MaterialTheme.colorScheme.onTertiary
            )

            Text(
                text = l,
                style = labelStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    )
            )
            Spacer(Modifier.height(32.dp))
        }

        PoshSoftKeyEntry(
            currentInputText = currentInputText
        )

        Spacer(Modifier.height(72.dp))
        
        buttonKeys.forEachIndexed { _, keys ->
            PoshSoftKeyRow(
                currentInputText = currentInputText,
                enabled = enabled,
                keys = keys,
                onClick = onClick,
            )
            Spacer(Modifier.height(DEFAULT_KEYPAD_ROW_VERTICAL_SEPARATOR))
        }
        PoshSoftKeyRow(
            currentInputText = currentInputText,
            enabled = enabled,
            onClick = onClick,
        )
        if(useFingerprint) {
            Spacer(Modifier.height(DEFAULT_KEYPAD_ROW_VERTICAL_SEPARATOR))
            PoshSoftKeyItem(
                enabled = enabled,
                icon = PoshIcon.Fingerprint,
                onClick = onClick,
                isExtended = true,
                currentInputText = currentInputText
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun PoshSoftKeyRow(
    currentInputText: String,
    enabled: Boolean,
    icon: Int = PoshIcon.Close,
    key: String = "0",
    onClick: (String?) -> Unit,
) {
    val mModifier: Modifier = Modifier.fillMaxWidth()
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 16.dp
    val expectedExtendedButtonWidth = (maxWidth * 2) / 3
    val expectedButtonWidth = (maxWidth / 3) - (DEFAULT_KEYPAD_ROW_HORIZONTAL_SEPARATOR / 2)

    Row(
        modifier = mModifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PoshSoftKeyItem(
            modifier = Modifier,
            enabled = enabled,
            currentInputText = currentInputText,
            key = key,
            onClick = onClick,
            width = expectedExtendedButtonWidth
        )
        Spacer(Modifier.width(DEFAULT_KEYPAD_ROW_HORIZONTAL_SEPARATOR))
        PoshSoftKeyItem(
            modifier = Modifier,
            enabled = enabled,
            icon = icon,
            onClick = onClick,
            width = expectedButtonWidth,
            currentInputText = currentInputText
        )
    }
}

@Composable
private fun PoshSoftKeyRow(
    currentInputText: String,
    enabled: Boolean,
    keys: Array<String>,
    onClick: (String) -> Unit,
) {
    val mModifier: Modifier = Modifier.fillMaxWidth()
    val columnCount = keys.size
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 16.dp
    val expectedButtonWidth = (maxWidth / columnCount) - (DEFAULT_KEYPAD_ROW_HORIZONTAL_SEPARATOR / 2)

    Row(
        modifier = mModifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(columnCount) { index ->
            PoshSoftKeyItem(
                modifier = Modifier,
                enabled = enabled,
                currentInputText = currentInputText,
                key = keys[index],
                onClick = onClick,
                width = expectedButtonWidth
            )
            if(index < columnCount - 1) {
                Spacer(Modifier.width(DEFAULT_KEYPAD_ROW_HORIZONTAL_SEPARATOR))
            }
        }
    }
}

@Composable
private fun PoshSoftKeyItem(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    currentInputText: String,
    icon: Int,
    width: Dp = DEFAULT_BUTTON_WIDTH,
    height: Dp = DEFAULT_BUTTON_HEIGHT,
    onClick: (String?) -> Unit,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    isExtended: Boolean = false,
) {
    val shape = if(isExtended) RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 22.dp,
        bottomEnd = 22.dp,
    )
    else MaterialTheme.shapes.extraSmall
    val background = MaterialTheme.colorScheme.primary
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 16.dp
    val extendedButtonWidth = maxWidth - 0.dp

    val mOnClick: () -> Unit = {
        if(isExtended) onClick.invoke(null) else onClick.invoke(currentInputText.dropLast(1))
    }

    val mModifier: Modifier = if(isExtended)
        Modifier
            .background(background, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .width(extendedButtonWidth)
            .height(height)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { mOnClick.invoke() },
                role = Role.Button,
            )
            .then(modifier)
    else
        Modifier
            .background(background, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .width(width)
            .height(height)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { mOnClick.invoke() },
                role = Role.Button,
            )
            .then(modifier)

    Box(
        modifier = mModifier,
       contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(DEFAULT_ICON_SIZE)
        )
    }
}


@Composable
private fun PoshSoftKeyItem(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    currentInputText: String,
    key: String,
    width: Dp = DEFAULT_BUTTON_WIDTH,
    height: Dp = DEFAULT_BUTTON_HEIGHT,
    onClick: (String) -> Unit,
    style: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary,
    ),
) {
    val shape = MaterialTheme.shapes.extraSmall
    val background = MaterialTheme.colorScheme.primary

    Box(
        Modifier
            .background(background, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary,shape)
            .width(width)
            .height(height)
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { if(currentInputText.length <= MAX_ALLOWED_PIN_LENGTH) onClick.invoke(currentInputText.plus(key)) },
                role = Role.Button,
            )
            .then(modifier), contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            style = style,
            modifier = Modifier.padding(4.dp)
        )
    }
}


@Composable
private fun PoshSoftKeyEntry(
    currentInputText: String
) {
    val mModifier: Modifier = Modifier.fillMaxWidth()
    val length = currentInputText.length
    val size = 12.dp

    Row(
        modifier = mModifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(MAX_ALLOWED_PIN_LENGTH) { index ->
            val isHighlighted = (index + 1) <= length
            val containerColorAnim by animateColorAsState(
                targetValue = if(isHighlighted) MaterialTheme.colorScheme.primary
                else Color.Transparent,
                label = "ColorAnim"
            )
            val borderColor = MaterialTheme.colorScheme.secondary

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(
                        color = containerColorAnim,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = CircleShape,
                    )
                    .size(size),
            )

            if(index < length - 1) {
                Spacer(Modifier.width(4.dp))
            }
        }
    }
}

@Composable
@Preview
private fun PoshSoftKeyPreview() {
    MainAppTheme {
        PoshSoftKeyItem(
            currentInputText = "A good",
            key = "8",
            onClick = {},
            enabled = true,
        )
    }
}

@Composable
@Preview
private fun PoshSoftKeyItemPreview() {
    MainAppTheme {
        PoshSoftKeyItem(
            enabled = true,
            onClick = {},
            icon = PoshIcon.Close,
         currentInputText = "",
        )
    }
}

@Composable
@Preview
private fun PoshSoftKeyItemExtendedPreview() {
    MainAppTheme {
        PoshSoftKeyItem(
            enabled = true,
            icon = PoshIcon.Fingerprint,
            onClick = {},
            isExtended = true,
            currentInputText = ""
        )
    }
}

@Composable
@Preview
private fun PoshSoftKeyRowPreview() {
    MainAppTheme {
        PoshSoftKeyRow(
            currentInputText = "",
            enabled = true,
            keys = arrayOf("4","5","6"),
            onClick = {}
        )
    }
}


@Composable
@Preview
private fun PoshSoftKeyRowLastRowPreview() {
    MainAppTheme {
        PoshSoftKeyRow(
            currentInputText = "",
            enabled = true,
            onClick = {}
        )
    }
}


@Composable
@Preview
private fun PoshSoftKeyboardPreview() {
    MainAppTheme {
        PoshSoftKeyboard(
            currentInputText = "",
            enabled = true,
            onClick = {},
            useFingerprint = true,
            label = "Enter username",
            paddingValues = PaddingValues()
        )
    }
}

@Composable
@Preview
private fun PoshSoftKeyEntryPreview() {
    MainAppTheme {
        PoshSoftKeyEntry(
            currentInputText = "ba"
        )
    }
}


