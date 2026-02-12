package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme

@Composable
fun PoshCheckboxWithText(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
    onClickText: () -> Unit,
) {
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        MaterialTheme.colorScheme.onTertiary
    )
    val clickableTextStyle = textStyle.copy(
        MaterialTheme.colorScheme.secondary
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PoshCheckbox(isChecked = isChecked, onCheckedChange = onCheckedChange, enabled = enabled)
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.i_agree),
            style = textStyle,
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(id = R.string.terms_and_privacy_policy),
            style = clickableTextStyle,
            modifier = Modifier
                .clickable(
                    enabled = enabled,
                    onClick = onClickText
                )
        )
    }
}

@Composable
fun PoshCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
) {
    val colors = CheckboxDefaults.colors(
        checkedColor = MaterialTheme.colorScheme.secondary,
        uncheckedColor = MaterialTheme.colorScheme.outlineVariant,
        checkmarkColor = MaterialTheme.colorScheme.secondary,
        disabledCheckedColor = MaterialTheme.colorScheme.inverseSurface,
        disabledUncheckedColor = MaterialTheme.colorScheme.surfaceTint
    )

    Checkbox(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = colors,
        enabled = enabled,
    )
}


@Composable
@Preview
private fun PoshCheckboxPreview() {
    MainAppTheme {
        PoshCheckbox(
            isChecked = false,
            onCheckedChange = {},
            enabled = true,
        )
    }
}

@Composable
@Preview
private fun PoshCheckboxWithTextPreview() {
    MainAppTheme {
        PoshCheckboxWithText(
            isChecked = true,
            onCheckedChange = {},
            enabled = true,
            onClickText = {}
        )
    }
}