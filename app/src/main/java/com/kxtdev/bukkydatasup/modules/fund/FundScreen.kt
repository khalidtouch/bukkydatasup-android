package com.kxtdev.bukkydatasup.modules.fund

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.enums.MerchantBank
import com.kxtdev.bukkydatasup.common.models.AppUiState
import com.kxtdev.bukkydatasup.common.models.UserBankResponse
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshNoticeThumbnail
import com.kxtdev.bukkydatasup.ui.design.PoshRebrandedListItem
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.design.RebrandedPoshWalletCard
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle


@Composable
fun FundScreen(
    onBackPressed: () -> Unit,
    onGenerateAccount: () -> Unit,
    onCopyAccountNumber: (String) -> Unit,
    onClickFundOption: (FundOption) -> Unit,
    banks: List<UserBankResponse>,
    appUiState: AppUiState,
) {
    val background = MaterialTheme.colorScheme.background

    var isViewActive by remember { mutableStateOf(false) }

    LaunchedEffect(banks.size) {
        isViewActive = true
    }

    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = stringResource(id = R.string.fund_wallet)

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
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding -> 

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(innerPadding)
        ) {
            fundOptions(
                onClickFundOption = onClickFundOption,
                enabled = isViewActive,
                getFundingDisabledState = { fundingOption -> appUiState.getFundingDisabledState(fundingOption)  }
            )

            cards(
                banks = banks,
                onCopy = onCopyAccountNumber,
                onGenerateAccount = onGenerateAccount,
                enabled = isViewActive,
                isLoading = banks.isEmpty(),
                isVerified = true,
            )

            item {
                Spacer(Modifier.height(102.dp))
            }

        }

    }


}

private fun LazyListScope.fundOptions(
    onClickFundOption: (FundOption) -> Unit,
    enabled: Boolean,
    getFundingDisabledState: (fundOption: FundOption) -> Boolean?,
) {
    item {

        val textStyle = MaterialTheme.typography.titleMedium.copy(
            MaterialTheme.colorScheme.onPrimary
        )

        Spacer(Modifier.height(32.dp))
        FundOption.getAvailableOptions().forEach { option ->
            val isFundingDisabled = getFundingDisabledState(option) ?: false
            PoshRebrandedListItem(
                title = {
                    Text(
                        text = option.title,
                        style = textStyle
                    )
                },
                onClick = { onClickFundOption(option) },
                enabled = enabled && !isFundingDisabled,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    ),
                trailingIcon = {
                    Icon(
                        painterResource(id = PoshIcon.ArrowForward),
                        contentDescription = null,
                        tint = textStyle.color,
                    )
                }
            )
            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.height(16.dp))
    }
}


private fun LazyListScope.cards(
    banks: List<UserBankResponse>,
    onCopy: (String) -> Unit,
    onGenerateAccount: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    isVerified: Boolean,
) {

    item {
        val header: String = if(isVerified) stringResource(id = R.string.wire_transfer) else
            stringResource(id = R.string.account_not_verified)
        val subtitle: String = if(isVerified) stringResource(id = R.string.send_wire_transfer_to_below_accounts) else
            stringResource(id = R.string.kindly_click_to_start_verification)

        Spacer(Modifier.height(28.dp))

        PoshNoticeThumbnail(
            modifier = Modifier.fillMaxWidth()
                .padding(
                start = 16.dp,
                end = 16.dp
                ),
            header = header,
            subtitle = subtitle,
            icon = PoshIcon.Bell,
        )

    }

    if (!isVerified) {
        item {
            val textColor = MaterialTheme.colorScheme.onTertiary
            val textStyle = MaterialTheme.typography.titleSmall.copy(textColor)

            Spacer(Modifier.height(22.dp))

            PoshButton(
                text = stringResource(id = R.string.verify_accounts),
                enabled = enabled,
                onClick = onGenerateAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    ),
                role = ButtonRole.PRIMARY,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.verify_profile_to_get_virtual_account),
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    )
            )
            Spacer(Modifier.height(22.dp))
        }

    } else {

        item {
            Spacer(Modifier.height(22.dp))
        }

        items(banks) { bank ->
            MerchantBank.getBankFromCode(bank.bankCode.orEmpty())?.let { merchantBank ->
                RebrandedPoshWalletCard(
                    bank = merchantBank,
                    accountName = bank.accountName.orEmpty(),
                    accountNumber = bank.accountNumber.orEmpty(),
                    onCopy = onCopy,
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        if (isLoading || banks.isEmpty()) {
            items(2) {
                RebrandedPoshWalletCard(
                    bank = MerchantBank.WEMA,
                    accountName = "",
                    accountNumber = "",
                    onCopy = onCopy,
                )
                Spacer(Modifier.height(12.dp))
            }
        }

    }

    item {
        Spacer(Modifier.height(12.dp))
    }
}