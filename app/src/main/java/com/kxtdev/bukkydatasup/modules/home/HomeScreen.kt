package com.kxtdev.bukkydatasup.modules.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.MerchantBank
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.Advertisement
import com.kxtdev.bukkydatasup.common.models.AppUiState
import com.kxtdev.bukkydatasup.common.models.DataBucketItem
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState
import com.kxtdev.bukkydatasup.common.models.UserBankResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshAd
import com.kxtdev.bukkydatasup.ui.design.PoshAlertNotification
import com.kxtdev.bukkydatasup.ui.design.PoshBucketBalance
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshFloatingActionButton
import com.kxtdev.bukkydatasup.ui.design.PoshImage
import com.kxtdev.bukkydatasup.ui.design.PoshProductItem
import com.kxtdev.bukkydatasup.ui.design.PoshRebrandedWalletThumbnail
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.design.RebrandedPoshWalletCard
import com.kxtdev.bukkydatasup.ui.theme.Black
import com.kxtdev.bukkydatasup.ui.theme.Transparent
import kotlinx.coroutines.delay


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    goToFundWallet: () -> Unit,
    onChatSupport: () -> Unit,
    loggedInUserState: LoggedInUserState,
    onGenerateAccount: () -> Unit,
    onCopyAccountNumber: (String) -> Unit,
    onClickProduct: (Product) -> Unit,
    banks: List<UserBankResponse>,
    onSetTransactionPin: () -> Unit,
    appUiState: AppUiState,
    preferenceUiState: PreferenceUiState,
    advertisements: List<Advertisement>,
) {
    val contentColor = MaterialTheme.colorScheme.onPrimary

    val isViewActive by remember { mutableStateOf(true) }
    var shouldShowTransactionPinPrompt by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(Settings.TRANSACTION_PIN_PROMPT_DELAY)
        shouldShowTransactionPinPrompt = true
    }

    var isBalanceVisible by remember { mutableStateOf(false) }
    val visibilityIcon = if(isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    val username = loggedInUserState.userConfig.username

    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                modifier = Modifier.border(1.dp, Black),
                title = {


                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        username?.let { name ->
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .padding(top = 28.dp)
                            ) {
                                Text(username,
                                    style = MaterialTheme.typography.headlineLarge
                                        .copy(contentColor)
                                )

                                Spacer(Modifier.weight(1f))

                                IconButton(
                                    enabled = true,
                                    onClick = { isBalanceVisible = !isBalanceVisible }
                                ) {
                                    Icon(
                                        imageVector = visibilityIcon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                    }

                }
            )
        },
        floatingActionButton = {
            PoshFloatingActionButton(
                modifier = Modifier,
                onClick = onChatSupport,
            ) {
                Image(
                    painterResource(id = PoshImage.WhatsApp),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(38.dp),
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(top = 240.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                PoshAlertNotification(appUiState.alertNotification)
                Spacer(Modifier.height(4.dp))
            }

            services(
                enabled = isViewActive,
                onClick = onClickProduct,
                products = Product.getHomeServices(),
                getProductDisabledState = { prod -> appUiState.getProductDisabledState(prod) }
            )

            ads(advertisements = advertisements)

            cards(
                banks = banks,
                onCopy = onCopyAccountNumber,
                onGenerateAccount = onGenerateAccount,
                enabled = isViewActive,
                isLoading = banks.isEmpty(),
                isVerified = loggedInUserState.isUserVerified,
            )

            item {
                Spacer(Modifier.height(208.dp))
            }

        }


        Box(
            Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            PoshRebrandedWalletThumbnail(
                modifier = Modifier,
                visible = isBalanceVisible,
                balance = loggedInUserState.userConfig.walletBalance,
                bonus = loggedInUserState.userConfig.bonusBalance,
                fundWallet = goToFundWallet,
            )

        }


        loggedInUserState.isTransactionPinNotConfigured.let { notConfigured ->
            if(preferenceUiState.shouldEnablePassCode && shouldShowTransactionPinPrompt && notConfigured) {
                PoshSnackBar(
                    message = stringResource(id = R.string.transaction_not_set_yet),
                    label = stringResource(id = R.string.set_pin),
                    onAction = { onSetTransactionPin.invoke() },
                    withOffset = true,
                    paddingValues = innerPadding,
                )
            }
        }

    }
}


private fun LazyListScope.cards(
    banks: List<UserBankResponse>,
    onCopy: (String) -> Unit,
    onGenerateAccount: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    isVerified: Boolean?,
) {
    item {
        isVerified?.let { verified ->
            if (!verified) {
                val textColor = MaterialTheme.colorScheme.onSurface
                val textStyle = MaterialTheme.typography.titleSmall.copy(textColor)

                Spacer(Modifier.height(8.dp))
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
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(22.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Spacer(Modifier.width(16.dp))
            }

            items(banks) { bank ->
                MerchantBank.getBankFromCode(bank.bankCode.orEmpty())?.let { merchantBank ->
                    RebrandedPoshWalletCard(
                        bank = merchantBank,
                        accountName = bank.accountName.orEmpty(),
                        accountNumber = bank.accountNumber.orEmpty(),
                        onCopy = onCopy,
                    )
                    Spacer(Modifier.width(12.dp))
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
                    Spacer(Modifier.width(12.dp))
                }
            }

            item {
                Spacer(Modifier.width(16.dp))
            }
        }

        Spacer(Modifier.height(12.dp))
    }
}


@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.services(
    enabled: Boolean,
    onClick: (Product) -> Unit,
    products: List<Product>,
    getProductDisabledState: (Product) -> Boolean?,
) {
    item {
        val textColor = MaterialTheme.colorScheme.onSurface
        val headerStyle = MaterialTheme.typography.titleLarge.copy(textColor)

        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.services),
            style = headerStyle,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            products
                .forEachIndexed { _, product ->
                    val isProductDisabled = getProductDisabledState(product) ?: false
                    PoshProductItem(
                        product = product,
                        onClick = { onClick.invoke(product) },
                        enabled = enabled && !isProductDisabled,
                    )
                }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
private fun LazyListScope.dataBuckets(
    bucketItems: List<DataBucketItem>,
    isLoading: Boolean,
) {
    item {
        val textColor = MaterialTheme.colorScheme.onSurface
        val headerStyle = MaterialTheme.typography.titleSmall.copy(textColor)

        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.data_buckets),
            style = headerStyle,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (bucketItems.isEmpty() || isLoading) {
                repeat(6) {
                    PoshBucketBalance(
                        modifier = Modifier,
                        header = "",
                        value = "",
                        isLoading = true,
                    )
                }
            } else {
                bucketItems
                    .forEachIndexed { _, bucket ->
                        PoshBucketBalance(
                            modifier = Modifier,
                            header = bucket.title,
                            value = bucket.value,
                            isLoading = false,
                        )
                    }
            }
        }
    }
}

private fun LazyListScope.ads(
    advertisements: List<Advertisement>,
) {
    item {
        Spacer(Modifier.height(8.dp))
        PoshAd(ads = advertisements)
    }
}