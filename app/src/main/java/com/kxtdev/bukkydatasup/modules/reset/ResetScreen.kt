package com.kxtdev.bukkydatasup.modules.reset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.common.enums.ResetMode
import com.kxtdev.bukkydatasup.common.models.PreferenceScreenUiState
import com.kxtdev.bukkydatasup.common.utils.ActionButton
import com.kxtdev.bukkydatasup.common.utils.PasswordEntry
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSoftKeyboard
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun ResetScreen(
    onBackPressed: () -> Unit,
    preferenceScreenUiState: PreferenceScreenUiState,
    updatePassword: (String) -> Unit,
    onContinue: (next: ResetMode?) -> Unit,
    updateTransactionPin: (String) -> Unit,
) {
    var isViewActive by remember { mutableStateOf(false) }
    val header = preferenceScreenUiState.resetMode?.title.orEmpty()

    LaunchedEffect(
        preferenceScreenUiState.isLoading,
        preferenceScreenUiState.error
    ) {
        isViewActive = preferenceScreenUiState.isLoading != true &&
                preferenceScreenUiState.error == null
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when (preferenceScreenUiState.resetMode) {
            ResetMode.NEW_PIN, ResetMode.NEW_PIN_AGAIN -> {

                PoshScaffold { innerPadding ->

                    var pin by remember { mutableStateOf("") }

                    LaunchedEffect(pin) {
                        updateTransactionPin(pin)
                    }

                    LaunchedEffect(preferenceScreenUiState.resetMode) {
                        if (preferenceScreenUiState.resetMode == ResetMode.NEW_PIN_AGAIN &&
                            pin.length >= Settings.MAX_TRANSACTION_PIN_LENGTH
                        ) {
                            pin = ""
                        }
                    }

                    PoshSoftKeyboard(
                        currentInputText = pin,
                        enabled = isViewActive,
                        onClick = { pin = it.orEmpty() },
                        useFingerprint = false,
                        label = header,
                        paddingValues = innerPadding,
                    )
                }

            }
            ResetMode.PASSWORD_VERIFICATION, ResetMode.NEW_PASSWORD, ResetMode.NEW_PASSWORD_AGAIN -> {

                PoshScaffold(
                    toolbar = {
                        PoshToolbarLarge(
                            title = {
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
                                    Spacer(Modifier.height(32.dp))
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

                    var password by remember { mutableStateOf("") }

                    LaunchedEffect(password) {
                        updatePassword(password)
                    }

                    LaunchedEffect(preferenceScreenUiState.resetMode) {
                        password = ""
                    }

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Transparent),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(Modifier.height(220.dp))
                        PasswordEntry(
                            value = password,
                            onValueChange = { password = it },
                            enabled = isViewActive,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                ),
                        )
                        Spacer(Modifier.weight(1f))
                        Spacer(Modifier.height(12.dp))
                        ActionButton(
                            onClick = { onContinue.invoke(preferenceScreenUiState.nextResetMode) },
                            enabled = isViewActive && password.isNotBlank(),
                            label = preferenceScreenUiState.buttonLabel.orEmpty(),
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
            }

            null -> {
                PoshLoader()
            }
        }

        if(preferenceScreenUiState.isLoading == true) {
            PoshLoader(loadingMessage = preferenceScreenUiState.loadingMessage)
        }
    }
}