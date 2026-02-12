package com.kxtdev.bukkydatasup.modules.fund

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.models.AppUiState
import com.kxtdev.bukkydatasup.common.models.FundingRequest
import com.kxtdev.bukkydatasup.common.utils.ActionButton
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.modules.fund.vm.FundingUiState
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge

@Composable
fun FundDetailScreen(
    onBackPressed: () -> Unit,
    fundingUiState: FundingUiState,
    appUiState: AppUiState,
    fundingRequest: FundingRequest,
    onAction: () -> Unit,
    updateValue: (String) -> Unit,
    onHandleThrowable: () -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.primaryContainer
    var isViewActive by remember { mutableStateOf(false) }
    val selectedFundingOption = appUiState.selectedFundOption
    val buttonLabelRes: Int? = when(selectedFundingOption) {
        FundOption.FUND_WITH_COUPON -> R.string.claim
        FundOption.FUND_WITH_MONNIFY_CARD,
        FundOption.FUND_FROM_BONUS -> R.string.continue_text
        else -> null
    }

    var value by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(
        fundingUiState.isLoading,
        fundingUiState.error
    ) {
        isViewActive = fundingUiState.isLoading != true &&
                fundingUiState.error == null
    }

    LaunchedEffect(value) {
        updateValue.invoke(value)
    }


    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = appUiState.selectedFundOption?.title.orEmpty()
                    val headerStyle = MaterialTheme.typography.headlineMedium.copy(textColor)

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                    ) {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = header,
                            style = headerStyle,
                            textAlign = TextAlign.Start
                        )
                    }
                },
                navigation = {
                    Box(
                        Modifier.padding(top = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(
                            enabled = isViewActive,
                            onClick = onBackPressed
                        ) {
                            Icon(
                                painterResource(PoshIcon.ArrowBack),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(32.dp))
            EnterValue(
                value = value,
                onValueChange = { value = it },
                enabled = isViewActive,
                selectedFundOption = fundingRequest.fundOption,
            )
            Spacer(Modifier.weight(1f))
            buttonLabelRes?.let { res ->
                ActionButton(
                    onClick = onAction,
                    enabled = isViewActive && fundingRequest.isValid,
                    label = stringResource(id = res),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        )
                )
                Spacer(Modifier.height(72.dp))
            }
        }

        if(fundingUiState.isLoading == true) {
            PoshLoader(loadingMessage = fundingUiState.loadingMessage)
        }
        if(fundingUiState.error != null) {
            PoshSnackBar(
                message = fundingUiState.error.message.orEmpty(),
                onAction = onHandleThrowable,
                paddingValues = innerPadding,
            )
        }

    }
}

@Composable
private fun EnterValue(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    selectedFundOption: FundOption?,
) {
    val cardFunding = listOf(FundOption.FUND_WITH_MONNIFY_CARD, FundOption.FUND_FROM_BONUS)
    val isMoney = cardFunding.contains(selectedFundOption)

    var keyboardOptions by remember { mutableStateOf(KeyboardOptions()) }

    LaunchedEffect(selectedFundOption, value) {
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = if (selectedFundOption == FundOption.FUND_WITH_COUPON) KeyboardType.Text
            else KeyboardType.Decimal
        )
    }

    val keyboardActions = KeyboardActions()
    val contentColor = MaterialTheme.colorScheme.primary
    val entryStyle = MaterialTheme.typography.titleMedium.copy(contentColor)

    var leadingIcon: @Composable (() -> Unit)? = null

    if(cardFunding.contains(selectedFundOption)) {
        leadingIcon = {
            Text(
                text = stringResource(id = R.string.naira),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = entryStyle.color,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }

    val placeholder: @Composable () -> Unit = {
        val res: Int? = when(selectedFundOption) {
            FundOption.FUND_WITH_COUPON -> R.string.enter_coupon
            FundOption.FUND_WITH_MONNIFY_CARD,
            FundOption.FUND_FROM_BONUS -> R.string.enter_amount
            else -> null
        }
        res?.let { r ->
            Text(
                text = stringResource(id = r),
                style = entryStyle
            )
        }
    }

    val mOnValueChange: (String) -> Unit = { v ->
        when(selectedFundOption) {
            FundOption.FUND_WITH_COUPON -> {
                onValueChange(v)
            }
            FundOption.FUND_WITH_MONNIFY_CARD,
            FundOption.FUND_FROM_BONUS -> {
                if(value.length <= Settings.FUNDING_AMOUNT_SIZE) onValueChange(v)
            }
            else -> Unit
        }
    }

    Spacer(Modifier.height(16.dp))
    PoshOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        value = value,
        enabled = enabled,
        onValueChange = mOnValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = VisualTransformation.None,
        placeholder = placeholder,
        textStyle = entryStyle,
        leadingIcon = leadingIcon,
        isMoney = isMoney,
    )
    Spacer(Modifier.height(4.dp))
}