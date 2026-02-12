package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshButton

fun LazyListScope.actionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    label: String,
    role: ButtonRole = ButtonRole.PRIMARY,
) {
    item {
        PoshButton(
            text = label,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            role = role,
        )
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    label: String,
) {
    PoshButton(
        text = label,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        role = ButtonRole.PRIMARY,
    )
}