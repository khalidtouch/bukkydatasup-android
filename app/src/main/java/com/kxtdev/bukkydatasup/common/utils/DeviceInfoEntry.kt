package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.MeterType
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.ui.design.PoshAutoSizeText
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshInlineLoader
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField
import com.kxtdev.bukkydatasup.ui.design.PoshTextButton

fun LazyListScope.enterDeviceInfo(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    caption: String,
    placeholderText: String?,
    isLoading: Boolean? = null,
    customerName: String?,
    selectedMeterType: MeterType,
    product: Product,
    cableId: Int,
    discoId: Int,
    validateSmartCardNumber: (iuc: String, cableId: Int) -> Unit,
    validateMeter: (discoId: Int, meterNumber: String, meterType: String) -> Unit,
) {
    item {
        var keyboardOptions by remember { mutableStateOf(KeyboardOptions()) }
        val textColor = MaterialTheme.colorScheme.onTertiary
        val captionStyle = MaterialTheme.typography.labelLarge.copy(textColor)
        val valueStyle = MaterialTheme.typography.bodyLarge.copy(textColor)
        val customerNameTextColor = MaterialTheme.colorScheme.onPrimaryContainer
        val customerNameBackground = MaterialTheme.colorScheme.primaryContainer
        val customerNameTextStyle = MaterialTheme.typography.titleMedium.copy(customerNameTextColor)
        val shape = MaterialTheme.shapes.medium
        val configuration = LocalConfiguration.current
        val maxWidth = configuration.screenWidthDp.dp - 32.dp
        val maxCustomerNameWidth = maxWidth - 72.dp

        LaunchedEffect(value) {
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        }

        val placeholder: @Composable () -> Unit = {
            Text(
                text = placeholderText.orEmpty(),
                style = valueStyle.copy(color = valueStyle.color.copy(0.4f))
            )
        }

        var leadingIcon: @Composable (() -> Unit)? = null
        var trailingIcon: @Composable (() -> Unit)? = null

        val onVerify: () -> Unit = {
            when(product) {
                Product.CABLE -> {
                    validateSmartCardNumber.invoke(value,cableId)
                }
                Product.ELECTRICITY -> {
                    validateMeter.invoke(discoId,value,selectedMeterType.title)
                }
                else -> Unit
            }
        }

        if(isLoading == true) {
            leadingIcon = {
                PoshInlineLoader()
            }
        }

        trailingIcon = {
            PoshTextButton(
                modifier = Modifier.padding(4.dp),
                text = stringResource(id = R.string.verify),
                onClick = { onVerify.invoke() },
                enabled = value.isNotBlank() && isLoading != true,
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
                .padding(start = 16.dp, end = 16.dp),
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(),
            visualTransformation = VisualTransformation.None,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            textStyle = valueStyle
        )
        Spacer(Modifier.height(4.dp))
        AnimatedVisibility(visible = isLoading != true && !customerName.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                    .background(customerNameBackground, shape)
                    .border(2.dp, customerNameBackground, shape),
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
                    PoshAutoSizeText(
                        text = customerName.orEmpty(),
                        style = customerNameTextStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = customerNameTextStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxCustomerNameWidth)
                    )
                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    Icon(
                      painterResource(id = PoshIcon.Verified),
                        contentDescription = null,
                        tint = customerNameTextColor
                    )
                }
            }
        }

    }
}