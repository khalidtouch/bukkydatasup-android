package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField

fun LazyListScope.enterAmount(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    caption: String,
    isMoney: Boolean = true,
    placeholderText: String?,
) {
    item {
        var keyboardOptions by remember { mutableStateOf(KeyboardOptions()) }
        val textColor = MaterialTheme.colorScheme.onTertiary
        val captionStyle = MaterialTheme.typography.labelLarge.copy(textColor)
        val amountStyle = MaterialTheme.typography.titleMedium.copy(
            color = textColor,
            fontWeight = FontWeight.Bold,
        )

        LaunchedEffect(value) {
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        }

        val placeholder: @Composable () -> Unit = {
            Text(
                text = placeholderText.orEmpty(),
                style = amountStyle.copy(color = amountStyle.color.copy(0.4f))
            )
        }

        var leadingIcon: @Composable (() -> Unit)? = null

        if(isMoney) {
            leadingIcon = {
                Text(
                    text = stringResource(id = R.string.naira),
                    style = amountStyle.copy(fontWeight = FontWeight.ExtraBold)
                )
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
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(),
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            textStyle = amountStyle,
            isMoney = isMoney,
            visualTransformation = VisualTransformation.None,
        )
        Spacer(Modifier.height(8.dp))
    }
}

fun LazyListScope.showAmount(
    value: String,
    caption: String,
) {
    item {
        val textColor = MaterialTheme.colorScheme.onPrimaryContainer
        val captionStyle = MaterialTheme.typography.labelLarge.copy(
            MaterialTheme.colorScheme.onTertiary
        )
        val amountStyle = MaterialTheme.typography.titleMedium.copy(textColor)
        val background = MaterialTheme.colorScheme.primaryContainer
        val shape = MaterialTheme.shapes.medium

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
                .background(background, shape)
                .border(2.dp, textColor, shape),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                      start = 18.dp,
                      end = 18.dp,
                      top = 18.dp,
                      bottom = 18.dp,
                  ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(id = R.string.naira),
                    style = amountStyle.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.width(22.dp))
                Text(
                    text = value,
                    style = amountStyle,
                    textAlign = TextAlign.Start
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
