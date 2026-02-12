package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField


@Composable
fun ColumnScope.PasswordEntry(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    var isVisible by remember { mutableStateOf(false) }

    val toggleVisibility: (Boolean) -> Unit = { visible ->
        isVisible = !visible
    }

    val passwordKeyboardOptions =
        KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)
    val passwordKeyboardActions = KeyboardActions()
    val passwordVisualTransformation = if (isVisible) VisualTransformation.None else
        PasswordVisualTransformation('*')

    val contentColor = MaterialTheme.colorScheme.onTertiary
    val hintStyle = MaterialTheme.typography.bodyLarge.copy(contentColor)

    val placeholder: @Composable (text: String) -> Unit = { t ->
        Text(
            text = t,
            style =  MaterialTheme.typography.bodyLarge
        )
    }

    val passwordTrailingIcon: @Composable () -> Unit = {
        if (isVisible) {
            IconButton(
                onClick = {  toggleVisibility(isVisible) },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = contentColor
                )
            }

        } else {
            IconButton(
                onClick = { toggleVisibility(isVisible) },
                enabled = enabled,
            ) {
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    }

    Spacer(Modifier.height(32.dp))
    Text(
        text = stringResource(id = R.string.password),
        style = hintStyle,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    )
    Spacer(Modifier.height(4.dp))
    PoshOutlinedTextField(
        modifier = modifier,
        enabled = enabled,
        value = value,
        onValueChange =  onValueChange,
        keyboardOptions = passwordKeyboardOptions,
        keyboardActions = passwordKeyboardActions,
        trailingIcon = passwordTrailingIcon,
        placeholder = { placeholder.invoke(stringResource(id = R.string.enter_password)) },
        visualTransformation = passwordVisualTransformation
    )
    Spacer(Modifier.height(12.dp))
}


fun LazyListScope.passwordEntry(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
) {
    item {
        var isVisible by remember { mutableStateOf(false) }

        val toggleVisibility: (Boolean) -> Unit = { visible ->
            isVisible = !visible
        }

        val passwordKeyboardOptions =
            KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)
        val passwordKeyboardActions = KeyboardActions()
        val passwordVisualTransformation = if (isVisible) VisualTransformation.None else
            PasswordVisualTransformation('*')

        val contentColor = MaterialTheme.colorScheme.onTertiary
        val hintStyle = MaterialTheme.typography.bodyLarge.copy(contentColor)

        val placeholder: @Composable (text: String) -> Unit = { t ->
            Text(
                text = t,
                style =  MaterialTheme.typography.bodyLarge
            )
        }

        val passwordTrailingIcon: @Composable () -> Unit = {
            if (isVisible) {
                IconButton(
                    onClick = {  toggleVisibility(isVisible) },
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = contentColor
                    )
                }

            } else {
                IconButton(
                    onClick = { toggleVisibility(isVisible) },
                    enabled = enabled,
                ) {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.password),
            style = hintStyle,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(Modifier.height(4.dp))
        PoshOutlinedTextField(
            modifier = modifier,
            enabled = enabled,
            value = value,
            onValueChange =  onValueChange,
            keyboardOptions = passwordKeyboardOptions,
            keyboardActions = passwordKeyboardActions,
            trailingIcon = passwordTrailingIcon,
            placeholder = { placeholder.invoke(stringResource(id = R.string.enter_password)) },
            visualTransformation = passwordVisualTransformation
        )
        Spacer(Modifier.height(12.dp))
    }
}