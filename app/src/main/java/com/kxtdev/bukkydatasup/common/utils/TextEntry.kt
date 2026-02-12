package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedMultipleTextTextField
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField

@Composable
fun ColumnScope.EnterText(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    caption: String,
    placeholderText: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
) {
    val textColor = MaterialTheme.colorScheme.onTertiary
    val captionStyle = MaterialTheme.typography.labelLarge.copy(textColor)
    val textStyle = MaterialTheme.typography.bodyLarge.copy(textColor)

    val placeholder: @Composable () -> Unit = {
        Text(
            text = placeholderText.orEmpty(),
            style = textStyle.copy(color = textStyle.color.copy(0.4f))
        )
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
        textStyle = textStyle,
        visualTransformation = VisualTransformation.None,
    )
    Spacer(Modifier.height(8.dp))
}


fun LazyListScope.enterText(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    caption: String,
    placeholderText: String?,
    isMultilineText: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
) {
    item {
        val textColor = MaterialTheme.colorScheme.onTertiary
        val captionStyle = MaterialTheme.typography.labelLarge.copy(
            MaterialTheme.colorScheme.secondary
        )
        val textStyle = MaterialTheme.typography.bodyLarge.copy(textColor)

        val textFieldModifier: Modifier = if (isMultilineText)
            Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(start = 16.dp, end = 16.dp)
        else Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)

        val placeholder: @Composable () -> Unit = {
            Text(
                text = placeholderText.orEmpty(),
                style = textStyle.copy(color = textStyle.color.copy(0.4f))
            )
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
            modifier = textFieldModifier,
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(),
            placeholder = placeholder,
            textStyle = textStyle,
            visualTransformation = VisualTransformation.None,
            isMultiline = isMultilineText,
        )
        Spacer(Modifier.height(8.dp))
    }
}


fun LazyListScope.enterMultipleText(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onChipClick: (Int) -> Unit,
    placeholderText: String,
    generatedTags: HashMap<Int, String>,
    onPickContact: () -> Unit,
) {
    item {
        val focusRequester = remember { FocusRequester() }
        val textInteraction = remember { MutableInteractionSource() }
        val rowInteraction = remember { MutableInteractionSource() }

        Spacer(Modifier.height(18.dp))

        PoshOutlinedMultipleTextTextField(
            textFieldValue = textFieldValue,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            textFieldInteraction = textInteraction,
            placeholder = placeholderText,
            rowInteraction = rowInteraction,
            onChipClick = onChipClick,
            listOfChips = generatedTags,
            onPickContact = onPickContact,
        )
    }
}

