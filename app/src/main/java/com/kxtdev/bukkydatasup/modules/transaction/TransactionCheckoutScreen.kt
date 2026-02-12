package com.kxtdev.bukkydatasup.modules.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.CheckoutState
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState
import com.kxtdev.bukkydatasup.common.utils.ActionButton
import com.kxtdev.bukkydatasup.common.utils.CheckoutSummary
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionUiState
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshSoftKeyboard
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.Transparent
import kotlinx.coroutines.delay

@Composable
fun TransactionCheckoutScreen(
    onBackPressed: () -> Unit,
    checkoutParams: HashMap<String, String>,
    onAction: () -> Unit,
    transactionUiState: TransactionUiState,
    onEngageBiometrics: () -> Unit,
    preferenceUiState: PreferenceUiState,
    updatePin: (String) -> Unit,
    onHandleThrowable: () -> Unit,
) {
    val isViewActive by remember { mutableStateOf(true) }
    val actionButtonLabel = stringResource(id = R.string.checkout)

    when(transactionUiState.checkoutState) {
        CheckoutState.INFORMATION -> {

            PoshScaffold(
                toolbar = {
                    PoshToolbarLarge(
                        title = {
                            val header = stringResource(id = R.string.checkout)

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                    ),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = header,
                                    style = AppHeaderStyle,
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
                        .padding(innerPadding)
                        .background(Transparent)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(Modifier.height(75.dp))
                    CheckoutSummary(params = checkoutParams)
                    Spacer(Modifier.height(22.dp))
                    Spacer(Modifier.weight(1f))
                    ActionButton(
                        onClick = onAction,
                        enabled = isViewActive,
                        label = actionButtonLabel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(102.dp))
                }
            }
        }
        CheckoutState.SECURITY -> {
            PoshScaffold {  innerPadding ->

                var pin by remember { mutableStateOf("") }

                LaunchedEffect(pin) {
                    updatePin(pin)
                    delay(Settings.BIOMETRICS_DELAY)
                    if(pin.length >= Settings.MAX_TRANSACTION_PIN_LENGTH) pin= ""
                }

                PoshSoftKeyboard(
                    currentInputText = pin,
                    enabled = isViewActive,
                    onClick = { pin = it.orEmpty(); if(it == null) onEngageBiometrics.invoke() },
                    useFingerprint = preferenceUiState.shouldEnableBiometrics,
                    label = stringResource(id = R.string.unlock_to_continue),
                    paddingValues = innerPadding,
                )

            }
        }
        else -> Unit
    }


    if(transactionUiState.isLoading == true) {
        PoshLoader(loadingMessage = transactionUiState.loadingMessage)
    }

    if(transactionUiState.error != null) {
        PoshSnackBar(
            message = transactionUiState.error.message.orEmpty(),
            onAction = onHandleThrowable,
            paddingValues = PaddingValues()
        )
    }

}