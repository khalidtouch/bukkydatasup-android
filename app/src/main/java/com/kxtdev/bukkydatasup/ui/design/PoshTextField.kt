package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.asThousand
import com.kxtdev.bukkydatasup.common.utils.clearThousand

@Composable
fun PoshOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    visualTransformation: VisualTransformation,
    isMultiline: Boolean = false,
    isMoney: Boolean = false,
) {
    var mMoneyValue by remember {
        mutableStateOf(TextFieldValue(value))
    }

    val mOnMoneyValueChange: (TextFieldValue) -> Unit = { moneyValue ->
        if(moneyValue.text.length <= Settings.FUNDING_AMOUNT_SIZE) {
            try {
                val newMoneyValue = if(moneyValue.text.isNotBlank()) {
                    moneyValue.text
                        .clearThousand()
                        .toBigDecimal()
                        .asThousand()
                } else moneyValue.text

                mMoneyValue = moneyValue.copy(
                    text = newMoneyValue,
                    selection = TextRange(newMoneyValue.length)
                )
            } catch (e: Exception) {
                mMoneyValue = moneyValue.copy(
                    text = "0",
                    selection = TextRange(1)
                )
            }
        }
    }

    LaunchedEffect(mMoneyValue) {
        onValueChange(mMoneyValue.text.clearThousand())
    }

    val shape = MaterialTheme.shapes.medium
    val colors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(0.4f),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        ),
        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.outline,
    )

    if(isMoney) {
        OutlinedTextField(
            value = mMoneyValue,
            onValueChange = mOnMoneyValueChange,
            modifier = modifier,
            textStyle = textStyle,
            singleLine = !isMultiline,
            maxLines = if(!isMultiline) 1 else 1000,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,
            colors = colors,
            enabled = enabled,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Decimal),
            keyboardActions = keyboardActions,
            placeholder = placeholder,
            visualTransformation = visualTransformation
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            textStyle = textStyle,
            singleLine = !isMultiline,
            maxLines = if(!isMultiline) 1 else 1000,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,
            colors = colors,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder,
            visualTransformation = visualTransformation
        )
    }

}


@Composable
fun PoshOutlinedMultipleTextTextField(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    textFieldInteraction: MutableInteractionSource,
    placeholder: String,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done
    ),
    rowInteraction: MutableInteractionSource,
    listOfChips: HashMap<Int, String> = hashMapOf(),
    onChipClick: (Int) -> Unit,
    onPickContact: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                vertical = 10.dp,
                horizontal = 20.dp,
            )
            .clickable(
                indication = null,
                interactionSource = rowInteraction,
                onClick = {
                    focusRequester.requestFocus()
                    keyboardManager?.show()
                }
            )
    ) {
        PoshMultipleTextTextFieldContent(
            textFieldValue = textFieldValue,
            placeholder = placeholder,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            textFieldInteraction = textFieldInteraction,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            focusManager = focusManager,
            listOfChips = listOfChips,
            modifier = modifier,
            onChipClick = onChipClick,
            onPickContact = onPickContact,
        )
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PoshMultipleTextTextFieldContent(
    textFieldValue: TextFieldValue,
    placeholder: String,
    onValueChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    textFieldInteraction: MutableInteractionSource,
    readOnly: Boolean,
    keyboardOptions: KeyboardOptions,
    focusManager: FocusManager,
    listOfChips: HashMap<Int, String>,
    modifier: Modifier,
    onChipClick: (Int) -> Unit,
    onPickContact: () -> Unit,
) {
    val shape = MaterialTheme.shapes.medium
    val isFocused = textFieldInteraction.collectIsFocusedAsState()
    val containerColor: Color = Color.Transparent
    val borderStroke = BorderStroke(width = 2.dp, color = if(isFocused.value)
        MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(0.2f)
    )

    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onTertiary,
        fontWeight = FontWeight.Bold,
    )
    val derivedPlaceholderStyle = textStyle.copy(color = textStyle.color.copy(0.4f))

    Box(
        modifier
            .background(containerColor, shape)
            .border(borderStroke, shape),
        contentAlignment = Alignment.CenterStart
    ) {
        if(textFieldValue.text.isEmpty() && listOfChips.isEmpty()) {
            Text(
                text = placeholder,
                style = derivedPlaceholderStyle,
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)
                    .padding(
                        start = 10.dp
                    )
            )
        }


        FlowRow(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp),
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Start
        ) {
            listOfChips.keys.forEachIndexed { _, k ->
                PoshIconChip(
                    modifier = Modifier.padding(8.dp),
                    enabled = true,
                    borderColor = derivedPlaceholderStyle.color,
                    label = listOfChips[k].orEmpty(),
                    icon = PoshIcon.Close,
                    onClick = {
                        onChipClick(k)
                        if(isFocused.value){
                            focusManager.clearFocus()
                        } else {
                            focusRequester.requestFocus()
                        }
                    }
                )
            }
            Spacer(Modifier.width(4.dp))
            BasicTextField(
                value = textFieldValue,
                onValueChange = onValueChange,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .width(IntrinsicSize.Min),
                singleLine = false,
                textStyle = textStyle,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .defaultMinSize(minHeight = 48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier.wrapContentWidth(),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Row(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 4.dp)
                                    .wrapContentWidth()
                            ) {
                                innerTextField.invoke()
                            }
                        }
                    }
                },
                interactionSource = textFieldInteraction,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                readOnly = readOnly,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
        }


        Box(
            Modifier.matchParentSize().padding(top = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onPickContact) {
                Icon(
                    painterResource(id = PoshIcon.PickContact),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }

    }

}

