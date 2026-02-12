package com.kxtdev.bukkydatasup.modules.transaction.nav

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AirtimeType
import com.kxtdev.bukkydatasup.common.enums.BiometricsResult
import com.kxtdev.bukkydatasup.common.enums.CableTV
import com.kxtdev.bukkydatasup.common.enums.CheckoutState
import com.kxtdev.bukkydatasup.common.enums.DataPlanType
import com.kxtdev.bukkydatasup.common.enums.DiscoProvider
import com.kxtdev.bukkydatasup.common.enums.ExamType
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.enums.MeterType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.CablePlanItem
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.common.models.PinResponse
import com.kxtdev.bukkydatasup.common.utils.BiometricsPromptManager
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.WhatsAppHandler
import com.kxtdev.bukkydatasup.common.utils.appToast
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.main.MainViewModel
import com.kxtdev.bukkydatasup.modules.fund.nav.navigateToFundDetailScreen
import com.kxtdev.bukkydatasup.modules.fund.vm.FundViewModel
import com.kxtdev.bukkydatasup.modules.home.nav.navigateToHomeScreen
import com.kxtdev.bukkydatasup.modules.services.nav.navigateToServiceScreen
import com.kxtdev.bukkydatasup.modules.status.nav.navigateToStatusScreen
import com.kxtdev.bukkydatasup.modules.transaction.TransactionCheckoutScreen
import com.kxtdev.bukkydatasup.modules.transaction.TransactionReceiptScreen
import com.kxtdev.bukkydatasup.modules.transaction.TransactionScreen
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionViewModel
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.navigation.poshComposable

fun NavGraphBuilder.transactionScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    transactionViewModel: TransactionViewModel,
    clipboardManager: ClipboardManager,
    fundViewModel: FundViewModel,
) {
    poshComposable(route = AppNavigation.TransactionScreen.route) {
        val appUiState by mainViewModel.appUiState.collectAsStateWithLifecycle()
        val loggedInUserState by mainViewModel.loggedInUserState.collectAsStateWithLifecycle()
        val transactionUiState by transactionViewModel.transactionUiState.collectAsStateWithLifecycle()
        val transactionRequest by transactionViewModel.transactionRequest.collectAsStateWithLifecycle()
        val computedDataPlans by transactionViewModel.dataPlansCached.collectAsStateWithLifecycle()
        val computedDataPlanTypes by transactionViewModel.dataPlanTypesCached.collectAsStateWithLifecycle()
        val computedCablePlans by transactionViewModel.cablePlansCached.collectAsStateWithLifecycle()

        val airtimeTransactionTotalAmount by transactionViewModel.airtimeTotalAmount(loggedInUserState)
            .collectAsStateWithLifecycle()
        val resultCheckerTransactionTotalAmount by transactionViewModel.resultCheckerTotalAmount(loggedInUserState)
            .collectAsStateWithLifecycle()
        val computedPinResponses by transactionViewModel.computedPinResponses(loggedInUserState)
            .collectAsStateWithLifecycle()
        val cardPrintingTotalAmount by transactionViewModel.cardPrintingTotalAmount.collectAsStateWithLifecycle()
        val airtimeSwapTotalAmount by transactionViewModel.airtimeSwapTotalAmount(loggedInUserState)
            .collectAsStateWithLifecycle()
        val generatedTags by transactionViewModel.generatedTags.collectAsStateWithLifecycle()
        val currentTagKey by transactionViewModel.tagKey.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val selectedProduct = transactionRequest.product ?: Product.AIRTIME

        val fromDestination by transactionViewModel.fromDestinationRoute.collectAsStateWithLifecycle()

        var canAutoCheckout by remember { mutableStateOf(false) }

        val onBackPressed: () -> Unit = {
            when(fromDestination) {
                AppNavigation.ServiceScreen.route -> navController.navigateToServiceScreen()
                else -> navController.navigateToHomeScreen()
            }

        }

        val updatePhone: (String) -> Unit = { phone ->
            transactionViewModel.updateRecipientPhone(phone)
        }

        val onChangeDialogState: (Boolean) -> Unit = { state ->
            transactionViewModel.onChangeDialogState(state)
        }

        val updateNetwork: (Network) -> Unit = { network ->
            transactionViewModel.updateNetwork(network.id, network.capitalizedName)
        }

        val onChangeDialogItem: (String) -> Unit = { name ->
            transactionViewModel.onChangeDialogItem(name, selectedProduct)
        }

        val updateAirtimeType: (AirtimeType) -> Unit = { type ->
            transactionViewModel.updateAirtimeType(type)
        }

        val updateAmount: (String) -> Unit = { amount ->
            transactionViewModel.updateAmount(amount, selectedProduct)
        }

        val onCheckout: () -> Unit = {
            navController.navigateToTransactionCheckoutScreen()
        }

        val updateDataPlanType: (DataPlanType) -> Unit = { planType ->
            transactionViewModel.updateDataPlanType(planType)
        }

        val updateQuantity: (String) -> Unit = { q ->
            transactionViewModel.updateQuantity(q)
        }

        val onSelectDataPlan: (DataPlanItem) -> Unit = { plan ->
            transactionViewModel.updatePlan(
                plan.id!!,
                plan.planSize.orEmpty(),
                plan.planAmount?.toDouble() ?: 0.0,
                plan.validity.orEmpty()
            )

            canAutoCheckout = true
        }

        val updateDeviceNumber: (String) -> Unit = { number ->
            transactionViewModel.updateDeviceNumber(number)
        }

        val onSelectCablePlan: (CablePlanItem) -> Unit = { planItem ->
            transactionViewModel.updatePlan(
                planItem.id ?: -1,
                planItem.cablePackage.orEmpty(),
                planItem.planAmount?.toDouble() ?: 0.0,
                ""
            )

            canAutoCheckout = true
        }

        val updateMeterType: (MeterType) -> Unit = { type ->
            transactionViewModel.updateMeterType(type)
        }

        val onHandleThrowable: () -> Unit = {
            transactionViewModel.onHandledThrowable()
        }

        val updatePinResponse: (PinResponse) -> Unit = { pin ->
            transactionViewModel.updatePinResponse(pin)
        }

        val updateNameOnCard: (String) -> Unit = { name ->
            transactionViewModel.updateNameOnCard(name)
        }

        val updateUsername: (String) -> Unit = { name ->
            transactionViewModel.updateUsername(name)
        }

        val onClickToCopyReferralLink: () -> Unit = {
            val referralLinkData = ClipData.newPlainText("referralLink", loggedInUserState.referralLink)
            clipboardManager.setPrimaryClip(referralLinkData)
            appToast(context, context.getString(R.string.copied))
        }

        val onFundFromBonus: () -> Unit = {
            mainViewModel.updateFundOption(FundOption.FUND_FROM_BONUS)
            fundViewModel.updateFundOption(FundOption.FUND_FROM_BONUS)
            navController.navigateToFundDetailScreen()
        }

        val addToTag: (key: Int, value: String) -> Unit = { k, v ->
            transactionViewModel.addToTags(k,v)
        }

        val removeTag: (key: Int) -> Unit = { k ->
            transactionViewModel.removeFromTags(k)
        }

        val updateMessage: (String) -> Unit = { msg ->
            transactionViewModel.updateMessage(msg)
        }

        val validateSmartCardNumber: (iuc: String, cableId: Int) -> Unit = { device,id ->
            transactionViewModel.validateSmartCardNumber(device, id)
        }

        val validateMeter: (discoId: Int, meterNumber: String, meterType: String) -> Unit = { id, number, type ->
            transactionViewModel.validateMeter(
                id,
                number,
                type,
                loggedInUserState.userConfig.phone.orEmpty(),
            )
        }

        LaunchedEffect(canAutoCheckout) {
            if(canAutoCheckout && transactionRequest.isValid()) {
                onCheckout.invoke()
            }
            canAutoCheckout = false
        }

        LaunchedEffect(Unit) {
            transactionViewModel.updateCheckoutState(CheckoutState.INFORMATION)
            transactionViewModel.updateUserType(loggedInUserState.user)
        }

        LaunchedEffect(computedDataPlanTypes.size) {
            when(appUiState.product) {
                Product.DATA -> {
                    if(computedDataPlanTypes.isNotEmpty()) {
                        val type = computedDataPlanTypes.first()
                        transactionViewModel.updateDataPlanType(type)
                    }

                }
                else -> Unit
            }
        }

        LaunchedEffect(transactionRequest.networkName, transactionRequest.networkId) {
            when(appUiState.product) {
                Product.DATA -> {
                    if(computedDataPlanTypes.isNotEmpty()) {
                        val type = computedDataPlanTypes.first()
                        transactionViewModel.updateDataPlanType(type)
                    }

                }
                else -> Unit
            }
        }

        LaunchedEffect(appUiState.product) {
            transactionViewModel.initTransactionRequest(appUiState.product)
            transactionViewModel.updateTransactionProduct(appUiState.product)

            when(appUiState.product) {
                Product.AIRTIME, Product.AIRTIME_SWAP -> {
                    transactionViewModel.updateNetwork(
                        Network.MTN.id,
                        Network.MTN.capitalizedName
                    )
                    transactionViewModel.updateAirtimeType(AirtimeType.VTU)
                }
                Product.DATA -> {
                    transactionViewModel.updateNetwork(
                        Network.MTN.id,
                        Network.MTN.capitalizedName
                    )
                    transactionViewModel.updateDataPlanType(
                        DataPlanType.CG
                    )
                }
                Product.CABLE -> {
                    transactionViewModel.updateProvider(
                        CableTV.GOTV.id,
                        CableTV.GOTV.title,
                        appUiState.product
                    )
                }
                Product.ELECTRICITY -> {
                    transactionViewModel.updateProvider(
                        DiscoProvider.IKEJA_ELECTRIC.id,
                        DiscoProvider.IKEJA_ELECTRIC.title,
                        appUiState.product
                    )
                    transactionViewModel.updateMeterType(MeterType.PREPAID)
                    transactionViewModel.updateRecipientPhone(loggedInUserState.userConfig.phone.orEmpty())
                }
                Product.RESULT_CHECKER -> {
                    transactionViewModel.updateProvider(
                        ExamType.WAEC.id,
                        ExamType.WAEC.title,
                        appUiState.product
                    )
                }
                Product.PRINT_CARD -> {
                    transactionViewModel.updateProvider(
                        Network.MTN.id,
                        Network.MTN.title,
                        appUiState.product
                    )
                    transactionViewModel.updateNetwork(
                        Network.MTN.id,
                        Network.MTN.name
                    )
                }

                else -> Unit
            }
        }

        TransactionScreen(
            transactionRequest = transactionRequest,
            transactionUiState = transactionUiState,
            loggedInUserState = loggedInUserState,
            onBackPressed = onBackPressed,
            updatePhone = updatePhone,
            onChangeDialogState = onChangeDialogState,
            updateNetwork = updateNetwork,
            onChangeDialogItem = onChangeDialogItem,
            updateAirtimeType = updateAirtimeType,
            updateAmount = updateAmount,
            airtimeTransactionTotalAmount = airtimeTransactionTotalAmount,
            onCheckout = onCheckout,
            updateDataPlanType = updateDataPlanType,
            computedDataPlanTypes = computedDataPlanTypes,
            computedDataPlans = computedDataPlans,
            onSelectDataPlan = onSelectDataPlan,
            resultCheckerTotalAmount = resultCheckerTransactionTotalAmount,
            updateQuantity = updateQuantity,
            updateDeviceNumber = updateDeviceNumber,
            computedCablePlans = computedCablePlans,
            onSelectCablePlan = onSelectCablePlan,
            updateMeterType = updateMeterType,
            onHandleThrowable = onHandleThrowable,
            updatePinResponse = updatePinResponse,
            computedPinResponses = computedPinResponses,
            updateNameOnCard = updateNameOnCard,
            cardPrintingTotalAmount = cardPrintingTotalAmount,
            updateUsername = updateUsername,
            onClickToCopyReferralLink = onClickToCopyReferralLink,
            onFundFromBonus = onFundFromBonus,
            airtimeSwapTotalAmount = airtimeSwapTotalAmount,
            addToTag = addToTag,
            removeTag = removeTag,
            generatedTags = generatedTags,
            currentTagKey = currentTagKey,
            updateMessage = updateMessage,
            appUiState = appUiState,
            validateMeter = validateMeter,
            validateSmartCardNumber = validateSmartCardNumber
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavGraphBuilder.transactionCheckoutScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    transactionViewModel: TransactionViewModel,
    biometricsPromptManager: BiometricsPromptManager,
    applicationContext: Context,
) {
    poshComposable(route = AppNavigation.TransactionCheckoutScreen.route) {
        val context = LocalContext.current
        val loggedInUserState by mainViewModel.loggedInUserState.collectAsStateWithLifecycle()
        val transactionRequest by transactionViewModel.transactionRequest.collectAsStateWithLifecycle()
        val transactionUiState by transactionViewModel.transactionUiState.collectAsStateWithLifecycle()
        val preferenceUiState by mainViewModel.preferenceUiState.collectAsStateWithLifecycle()

        val biometricsTitle = stringResource(id = R.string.unlock_to_continue)
        val biometricsDescription = stringResource(R.string.scan_your_fingerprint)

        val biometricResult by biometricsPromptManager.promptResult.collectAsStateWithLifecycle(
            initialValue = null,
        )

        val selectedNetwork: Network = Network.getById(
            transactionRequest.networkId ?: 1
        )   ?: Network.MTN

        val selectedAirtimeType: AirtimeType = transactionRequest.airtimeType
            ?: AirtimeType.VTU

        val checkoutParams = transactionRequest.getCheckoutScreen(
            context,
            topUpPercentage = loggedInUserState.userConfig.topUpPercentage?.getTopUpPercentage(
                selectedNetwork,
                selectedAirtimeType
            ) ?: 0.0,
            airtimeSwapPercentage = loggedInUserState.getAirtimeSwapPercentage(
                selectedNetwork
            ) ?: 0.0,
        )

        val airtimeSwapPlaceholder: String = stringResource(
            id = R.string.airtime_swap_placeholder,
            loggedInUserState.userConfig.username.orEmpty(),
            transactionRequest.amount.toString().asMoney(),
            transactionRequest.networkName.toString(),
            transactionRequest.recipientPhone.orEmpty(),
        )

        val onChatSupport: () -> Unit = {
            WhatsAppHandler().apply {
                chatSupport(
                    context,
                    loggedInUserState.userConfig.supportPhoneNumber,
                    airtimeSwapPlaceholder
                )
            }
        }

        val onVerificationSucceeded: () -> Unit = {
            when(transactionRequest.product) {
                Product.AIRTIME -> {
                    transactionViewModel.topUpAirtime(mainViewModel::updateTransactionStatus)
                }
                Product.DATA -> {
                    transactionViewModel.buyData(mainViewModel::updateTransactionStatus)
                }
                Product.CABLE -> {
                    transactionViewModel.subscribeCable(mainViewModel::updateTransactionStatus)
                }
                Product.TRANSFER -> {
                    transactionViewModel.transferFunds(mainViewModel::updateTransactionStatus)
                }
                Product.ELECTRICITY -> {
                    transactionViewModel.subscribeBill(mainViewModel::updateTransactionStatus)
                }
                Product.RESULT_CHECKER -> {
                    transactionViewModel.checkResult(mainViewModel::updateTransactionStatus)
                }
                Product.PRINT_CARD -> {
                    transactionViewModel.printCard(mainViewModel::updateTransactionStatus)
                }
                Product.AIRTIME_SWAP -> {
                    onChatSupport.invoke()
                }
                Product.BULK_SMS -> {
                    transactionViewModel.sendBulkSMS(mainViewModel::updateTransactionStatus)
                }

                else -> Unit
            }
        }

        val onVerificationFailed: () -> Unit = {
            transactionViewModel.onVerificationFailed()
        }

        val onHandleThrowable: () -> Unit = {
            transactionViewModel.onHandledThrowable()
        }

        val onAction: () -> Unit = {
            if(preferenceUiState.shouldEnableTransactionPin) {
                val nextCheckoutState = transactionUiState.checkoutState?.nextState
               transactionViewModel.updateCheckoutState(nextCheckoutState)
            } else {
                onVerificationSucceeded.invoke()
            }
        }

        val onBackPressed: () -> Unit = {
            if(transactionUiState.checkoutState == CheckoutState.INFORMATION) {
                navController.navigateToTransactionScreen()
            }
            val prevCheckoutState = transactionUiState.checkoutState?.prevState
            transactionViewModel.updateCheckoutState(prevCheckoutState)
        }

        val updatePin: (String) -> Unit  = { p ->
            transactionViewModel.updateTransactionPin(p)
        }

        val onEngageBiometrics: () -> Unit = {
            biometricsPromptManager.showPrompt(
                biometricsTitle,
                biometricsDescription
            )
        }

        LaunchedEffect(biometricResult) {
            when(biometricResult) {
                is BiometricsResult.AuthenticationError -> {
                    appToast(applicationContext, "Error reading fingerprint")
                }
                is BiometricsResult.AuthenticationFailed -> {
                    appToast(applicationContext, "Failed")
                }
                is BiometricsResult.AuthenticationSuccess -> {
                    appToast(applicationContext, "Success!")
                    onVerificationSucceeded.invoke()
                }
                else -> Unit
            }
        }


        LaunchedEffect(transactionUiState.shouldShowStatusPage) {
            if(transactionUiState.shouldShowStatusPage == true) {
                transactionViewModel.updateShouldShowTransactionReceipt(true)
                navController.navigateToStatusScreen()
                transactionViewModel.updateStatusPageAs(null)
            }
        }

        LaunchedEffect(transactionUiState.transactionPin) {
            val proceed =
                transactionUiState.checkoutState == CheckoutState.SECURITY &&
                        transactionUiState.transactionPin != null &&
                        transactionUiState.transactionPin?.length!! >= Settings.MAX_TRANSACTION_PIN_LENGTH
            if(proceed) {
                transactionViewModel
                    .verifyPin(
                        pin = transactionUiState.transactionPin.orEmpty(),
                        onSuccess = { onVerificationSucceeded.invoke() },
                        onFailed = { onVerificationFailed.invoke() }
                    )
            }
        }

        TransactionCheckoutScreen(
            onBackPressed = onBackPressed,
            checkoutParams = checkoutParams,
            onAction = onAction,
            transactionUiState = transactionUiState,
            onEngageBiometrics = onEngageBiometrics,
            preferenceUiState = preferenceUiState,
            updatePin = updatePin,
            onHandleThrowable = onHandleThrowable,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}

fun NavGraphBuilder.transactionReceiptScreen(
    navController: NavHostController,
    transactionViewModel: TransactionViewModel,
    clipboardManager: ClipboardManager,
) {
    poshComposable(route = AppNavigation.TransactionReceiptScreen.route) {
        val transactionRequest by transactionViewModel.transactionRequest.collectAsStateWithLifecycle()
        val transactionUiState by transactionViewModel.transactionUiState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        val onCopy: (String) -> Unit = { data ->
            val accountNumberData = ClipData.newPlainText("receipt", data)
            clipboardManager.setPrimaryClip(accountNumberData)
            appToast(context, context.getString(R.string.copied))
        }

        val onHandleThrowable: () -> Unit = {
            transactionViewModel.onHandledThrowable()
        }

        val onBackPressed: () -> Unit = {
            navController.navigateToHomeScreen()
        }

        TransactionReceiptScreen(
            transactionUiState = transactionUiState,
            transactionRequest = transactionRequest,
            onBackPressed = onBackPressed,
            onHandleThrowable = onHandleThrowable,
            onCopy = onCopy,
        )

        BackHandler {
            onBackPressed.invoke()
        }
    }
}


fun NavController.navigateToTransactionScreen() {
    navigate(AppNavigation.TransactionScreen.route)
}

fun NavController.navigateToTransactionCheckoutScreen() {
    navigate(AppNavigation.TransactionCheckoutScreen.route)
}

fun NavController.navigateToTransactionReceiptScreen() {
    navigate(AppNavigation.TransactionReceiptScreen.route)
}
