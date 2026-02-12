package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField

fun LazyListScope.selectProvider(
    selectedProviderName: String?,
    onSelectProvider: (String) -> Unit,
    enabled: Boolean,
    selectedProviderIcon: Int?,
    caption: String,
) {
    item {
        val textColor = MaterialTheme.colorScheme.onTertiary
        val providerStyle = MaterialTheme.typography.titleSmall.copy(textColor)
        val captionStyle = MaterialTheme.typography.labelLarge.copy(textColor)

        val trailingIcon: @Composable () -> Unit = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = providerStyle.color
            )
        }

        val leadingIcon: @Composable () -> Unit = {
            selectedProviderIcon.let { icon ->
                selectedProviderName?.let { name ->
                    icon?.let { i ->
                        IconButton(onClick = { onSelectProvider(name) }) {
                            Image(
                                painterResource(id = i),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = caption,
            style = captionStyle,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                )
        )
        Spacer(Modifier.height(8.dp))
        PoshOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable(
                    enabled = enabled,
                    onClick = { selectedProviderName?.let(onSelectProvider) }
                ),
            value = selectedProviderName.orEmpty(),
            enabled = false,
            onValueChange = {},
            keyboardOptions = KeyboardOptions(),
            keyboardActions = KeyboardActions(),
            visualTransformation = VisualTransformation.None,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = providerStyle
        )
        Spacer(Modifier.height(4.dp))
    }
}
