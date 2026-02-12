package com.kxtdev.bukkydatasup.modules.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.ChildPreferenceItem
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.common.models.PreferenceScreenUiState
import com.kxtdev.bukkydatasup.common.models.SecurityRequest
import com.kxtdev.bukkydatasup.common.utils.ActionButton
import com.kxtdev.bukkydatasup.common.utils.EnterText
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshRebrandedListItem
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.Transparent

@Composable
fun PreferenceDetailScreen(
    preferenceScreenUiState: PreferenceScreenUiState,
    onBackPressed: () -> Unit,
    loggedInUserState: LoggedInUserState,
    onClickPreferenceDetailItem: (ChildPreferenceItem) -> Unit,
    securityRequest: SecurityRequest,
    onVerifyAccount: () -> Unit,
    updateFullname: (String) -> Unit,
    updateBvn: (String) -> Unit,
    updateNin: (String) -> Unit,
) {
    var isViewActive by remember { mutableStateOf(false) }

    val contentColor = MaterialTheme.colorScheme.onPrimary
    val textStyle = MaterialTheme.typography.titleMedium.copy(contentColor)

    LaunchedEffect(
        preferenceScreenUiState.isLoading,
        preferenceScreenUiState.error
    ) {
        isViewActive = preferenceScreenUiState.isLoading != true &&
                preferenceScreenUiState.error == null
    }


    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = preferenceScreenUiState.preferenceItem?.title.orEmpty()

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

        when (preferenceScreenUiState.preferenceItem) {
            PreferenceItem.UPGRADE_ACCOUNT -> {
                LazyColumn(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Transparent)
                        .padding(innerPadding)
                ) {
                    val upgradeItems = ChildPreferenceItem.getChildren(
                        PreferenceItem.UPGRADE_ACCOUNT.id
                    )

                    item {
                        Spacer(Modifier.height(12.dp))
                    }

                    var trailingIcon: @Composable (() -> Unit)? = null

                    items(upgradeItems) { upgradeItem ->

                        val title: @Composable (() -> Unit) = {
                            Text(
                                text = upgradeItem.title.uppercase(),
                                style = textStyle,
                                textAlign = TextAlign.Start,
                            )
                        }


                        when (upgradeItem) {
                            ChildPreferenceItem.AFFILIATE -> {
                                trailingIcon = {
                                    Text(
                                        text = stringResource(
                                            id = R.string.money,
                                            loggedInUserState.userConfig.affiliateUpgradeFee.toString()
                                                .asMoney()
                                        ),
                                        style = textStyle,
                                        textAlign = TextAlign.End,
                                    )
                                }
                            }

                            ChildPreferenceItem.TOP_USER -> {
                                trailingIcon = {
                                    Text(
                                        text = stringResource(
                                            id = R.string.money,
                                            loggedInUserState.userConfig.topUserUpgradeFee.toString().asMoney()
                                        ),
                                        style = textStyle,
                                        textAlign = TextAlign.End,
                                    )
                                }
                            }

                            else -> Unit
                        }


                        PoshRebrandedListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                ),
                            trailingIcon = trailingIcon,
                            title = title,
                            onClick = { onClickPreferenceDetailItem.invoke(upgradeItem) },
                            enabled = isViewActive,
                        )
                    }

                    item {
                        Spacer(Modifier.height(72.dp))
                    }
                }
            }

            PreferenceItem.VERIFY_ACCOUNT -> {
                var fullname by remember { mutableStateOf("") }
                var bvn by remember { mutableStateOf("") }
                var nin by remember { mutableStateOf("") }
                val instructionStyle = MaterialTheme.typography.bodyLarge.copy(
                    MaterialTheme.colorScheme.secondary
                )

                LaunchedEffect(fullname) {
                    updateFullname(fullname)
                }
                LaunchedEffect(bvn) {
                    updateBvn(bvn)
                }
                LaunchedEffect(nin) {
                    updateNin(nin)
                }

                Column(
                    Modifier.fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.account_verification_instructions),
                        style = instructionStyle,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(12.dp))
                    EnterText(
                        value = fullname,
                        onValueChange = { fullname = it },
                        enabled = isViewActive,
                        caption = stringResource(id = R.string.enter_fullname),
                        placeholderText = stringResource(id = R.string.fullname),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        )
                    )
                    EnterText(
                        value = bvn,
                        onValueChange = { bvn = it },
                        enabled = isViewActive,
                        caption = stringResource(id = R.string.enter_bvn),
                        placeholderText = stringResource(id = R.string.bvn),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.or),
                        style = instructionStyle.copy(color = MaterialTheme.colorScheme.onTertiary),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    EnterText(
                        value = nin,
                        onValueChange = { nin = it },
                        enabled = isViewActive,
                        caption = stringResource(id = R.string.enter_nin),
                        placeholderText = stringResource(id = R.string.nin),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Spacer(Modifier.weight(1f))
                    ActionButton(
                        onClick = onVerifyAccount,
                        enabled = isViewActive && securityRequest.canVerifyAccount,
                        label = stringResource(id = R.string.verify_account),
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

            else -> Unit
        }

    }

    if (preferenceScreenUiState.isLoading == true) {
        PoshLoader(loadingMessage = preferenceScreenUiState.loadingMessage)
    }
}