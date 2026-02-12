package com.kxtdev.bukkydatasup.modules.transaction

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AirtimeType
import com.kxtdev.bukkydatasup.common.enums.DataPlanType
import com.kxtdev.bukkydatasup.common.enums.DataPlanType.Companion.expressAsString
import com.kxtdev.bukkydatasup.common.enums.MeterType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.AppUiState
import com.kxtdev.bukkydatasup.common.models.CablePlanItem
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.common.models.PinResponse
import com.kxtdev.bukkydatasup.common.models.TransactionRequest
import com.kxtdev.bukkydatasup.common.models.expressAsStrings
import com.kxtdev.bukkydatasup.common.utils.FormUtil
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.actionButton
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.common.utils.convertMoneyToDouble
import com.kxtdev.bukkydatasup.common.utils.enterAmount
import com.kxtdev.bukkydatasup.common.utils.enterDeviceInfo
import com.kxtdev.bukkydatasup.common.utils.enterMultipleText
import com.kxtdev.bukkydatasup.common.utils.enterPhone
import com.kxtdev.bukkydatasup.common.utils.enterText
import com.kxtdev.bukkydatasup.common.utils.selectCablePlan
import com.kxtdev.bukkydatasup.common.utils.selectDataPlan
import com.kxtdev.bukkydatasup.common.utils.selectProvider
import com.kxtdev.bukkydatasup.common.utils.showAmount
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionUiState
import com.kxtdev.bukkydatasup.ui.PoshSwitch
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshAlertDialog
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.design.PoshTransactionWalletThumbnail
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.Transparent

val TAG = "TransactionScreen"

@Composable
fun TransactionScreen(
    transactionRequest: TransactionRequest,
    transactionUiState: TransactionUiState,
    loggedInUserState: LoggedInUserState,
    onBackPressed: () -> Unit,
    updatePhone: (String) -> Unit,
    onChangeDialogState: (Boolean) -> Unit,
    updateNetwork: (Network) -> Unit,
    onChangeDialogItem: (String) -> Unit,
    updateAirtimeType: (AirtimeType) -> Unit,
    updateAmount: (String) -> Unit,
    airtimeTransactionTotalAmount: Double,
    onCheckout: () -> Unit,
    updateDataPlanType: (DataPlanType) -> Unit,
    computedDataPlanTypes: List<DataPlanType>,
    computedDataPlans: List<DataPlanItem>,
    onSelectDataPlan: (DataPlanItem) -> Unit,
    resultCheckerTotalAmount: Double,
    updateQuantity: (String) -> Unit,
    updateDeviceNumber: (String) -> Unit,
    onSelectCablePlan: (CablePlanItem) -> Unit,
    computedCablePlans: List<CablePlanItem>,
    updateMeterType: (MeterType) -> Unit,
    onHandleThrowable: () -> Unit,
    computedPinResponses: List<PinResponse>,
    updatePinResponse: (PinResponse) -> Unit,
    updateNameOnCard: (String) -> Unit,
    cardPrintingTotalAmount: Double,
    updateUsername: (String) -> Unit,
    onClickToCopyReferralLink: () -> Unit,
    onFundFromBonus: () -> Unit,
    airtimeSwapTotalAmount: Double,
    addToTag: (key: Int, value: String) -> Unit,
    removeTag: (key: Int) -> Unit,
    generatedTags: HashMap<Int, String>,
    currentTagKey: Int,
    updateMessage: (String) -> Unit,
    appUiState: AppUiState,
    validateSmartCardNumber: (iuc: String, cableId: Int) -> Unit,
    validateMeter: (discoId: Int, meterNumber: String, meterType: String) -> Unit,
) {

    var isViewActive by remember { mutableStateOf(false) }
    var multipleTextFieldValue by remember(generatedTags) { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current

    val contactIntent = Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }

    val onContactActivityResult: (ActivityResult) -> Unit = { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = result.data?.data

            val projection: Array<String> = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
            )

            contactUri?.let {
                context.contentResolver.query(it, projection, null, null, null)
                    .use { cursor ->
                        cursor?.let { curs ->
                            if (curs.moveToFirst()) {
                                val numberIndex =
                                    curs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                var number = curs.getString(numberIndex)
                                number = number.replace("+234", "0").replace(" ", "")

                                val tags = FormUtil.splitPerPhoneLengthComplete(number)
                                multipleTextFieldValue = TextFieldValue(number)

                                if(tags.isNotEmpty()) {
                                    addToTag(currentTagKey, tags.first())
                                    multipleTextFieldValue = multipleTextFieldValue.copy(text = "")
                                }
                            }
                        }
                    }
            }
        }
    }

    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onContactActivityResult
    )

    val contactPermissionCallback: (Boolean) -> Unit = {
        resultLauncher.launch(contactIntent)
    }

    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = contactPermissionCallback
    )


    LaunchedEffect(
        transactionUiState.isLoading,
        transactionUiState.error
    ) {
        isViewActive = transactionUiState.isLoading != true &&
                transactionUiState.error == null
    }


    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {

                    val header = transactionRequest.product?.title.orEmpty()
                    val userBalance = loggedInUserState.userConfig.getBalance(context, transactionRequest.product ?: Product.AIRTIME)

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
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = header,
                            style = AppHeaderStyle,
                            textAlign = TextAlign.Start
                        )
                        Spacer(Modifier.height(6.dp))
                        PoshTransactionWalletThumbnail(
                            username = loggedInUserState.userConfig.username,
                            balance = userBalance.first,
                            enabled = isViewActive,
                            balanceLabel = userBalance.second
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


        var phone by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("") }
        var deviceNumber by remember { mutableStateOf("") }
        var nameOnCard by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        val placeholderText = stringResource(id = R.string.enter_recipient_phones)

        val amountCaption = stringResource(id = R.string.enter_amount)
        val totalAmountCaption = stringResource(id = R.string.total_amount)
        val airtimeTotalAmountString = airtimeTransactionTotalAmount.toString().asMoney()
        val quantityCaption = stringResource(id = R.string.quantity)
        val resultCheckerTotalAmountString = resultCheckerTotalAmount.toString().asMoney()
        val smartCardNumberCaption = stringResource(id = R.string.enter_smart_card_number)
        val meterNumberCaption = stringResource(id = R.string.enter_meter_number)
        val companyNamePlaceholder = stringResource(id = R.string.company_name)
        val nameOnCardCaption = stringResource(id = R.string.enter_name_on_card)
        val cardPrintingTotalAmountString = cardPrintingTotalAmount.toString().asMoney()
        val usernameCaption = stringResource(id = R.string.username)
        val enterUsernameCaption = stringResource(id = R.string.enter_recipient_username)
        val copyReferralLinkText = stringResource(id = R.string.click_to_copy_referral_link)
        val withdrawFromBonusText = stringResource(id = R.string.withdraw_from_bonus)
        val airtimeSwapTotalAmountString = airtimeSwapTotalAmount.toString().asMoney()
        val enterSenderNamePlaceholderText = stringResource(id = R.string.enter_sender_name)
        val enterMessageBodyPlaceholderText = stringResource(R.string.enter_message_body)

        LaunchedEffect(phone) {
            updatePhone(phone)
        }

        LaunchedEffect(amount) {
            updateAmount(amount)
        }

        LaunchedEffect(quantity) {
            updateQuantity(quantity)
        }

        LaunchedEffect(resultCheckerTotalAmount) {
            updateAmount(resultCheckerTotalAmount.toString())
        }

        LaunchedEffect(deviceNumber) {
            updateDeviceNumber(deviceNumber)
        }

        LaunchedEffect(nameOnCard) {
            updateNameOnCard(nameOnCard)
        }

        LaunchedEffect(username) {
            updateUsername(username)
        }

        LaunchedEffect(message) {
            updateMessage(message)
        }

        val restorePendingValues: () -> Unit = {
            when(transactionRequest.product) {
                Product.AIRTIME -> {


                    phone = transactionRequest.recipientPhone.orEmpty()
                    amount = transactionRequest.amount.toString().asMoney()
                }
                /*todo(): more products */
                else -> Unit
            }
        }

        LaunchedEffect(Unit) {
            restorePendingValues.invoke()
        }

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(innerPadding)
        ) {
            when(transactionRequest.product) {
                Product.AIRTIME, Product.DATA, Product.AIRTIME_SWAP -> {
                    enterPhone(
                        value = phone,
                        onValueChange = { phone = it },
                        selectedNetwork = transactionRequest.getSelectedNetwork(),
                        onShowDialog = { onChangeDialogState.invoke(transactionUiState.shouldShowDialog ?: false) },
                        updateNetwork = updateNetwork,
                        enabled = isViewActive
                    )
                }
                Product.RESULT_CHECKER,
                Product.CABLE,
                Product.ELECTRICITY,
                Product.PRINT_CARD -> {
                    selectProvider(
                        selectedProviderName = transactionRequest.selectedProviderName,
                        onSelectProvider = { onChangeDialogState.invoke(transactionUiState.shouldShowDialog ?: false) },
                        enabled = isViewActive,
                        selectedProviderIcon = transactionRequest.selectedProviderIcon,
                        caption = transactionRequest.providerCaption,
                    )
                }
                Product.TRANSFER -> {
                    enterText(
                        value = username,
                        onValueChange = { username = it },
                        enabled = isViewActive,
                        caption = usernameCaption,
                        placeholderText = enterUsernameCaption,
                    )
                }
                Product.REFER_FRIEND -> {
                    item {
                        Spacer(Modifier.height(102.dp))
                    }

                   actionButton(
                       onClick = onClickToCopyReferralLink,
                       enabled = isViewActive,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(
                               start = 16.dp,
                               end = 16.dp,
                           ),
                       label = copyReferralLinkText,
                   )
                }
                Product.BULK_SMS -> {

                    val onPickContact: () -> Unit = {
                        when(PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_CONTACTS
                            ) -> resultLauncher.launch(contactIntent)

                            else -> contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }


                    val onValueChange: (TextFieldValue) -> Unit = { v ->
                        val tags = FormUtil.splitPerPhoneLengthComplete(v.text)
                        multipleTextFieldValue = v

                        if(tags.isNotEmpty()) {
                            addToTag(currentTagKey, tags.first())
                            multipleTextFieldValue = multipleTextFieldValue.copy(text = "")
                        }

                    }

                    enterMultipleText(
                        textFieldValue = multipleTextFieldValue,
                        onValueChange = onValueChange,
                        onChipClick = removeTag,
                        placeholderText = placeholderText,
                        generatedTags = generatedTags,
                        onPickContact = onPickContact,
                    )
                }
                else -> Unit
            }

            when(transactionRequest.product) {
                Product.AIRTIME -> {
                    item {
                        Spacer(Modifier.height(12.dp))
                        PoshSwitch(
                            selectedOption = transactionRequest.selectedAirtimeType.title,
                            onSelect = { updateAirtimeType.invoke(AirtimeType.getByTitle(it)) },
                            options = AirtimeType.getOptionsAsString(),
                            enabled = isViewActive,
                        )
                    }
                }
                Product.DATA -> {
                    item {
                        Spacer(Modifier.height(12.dp))
                        PoshSwitch(
                            selectedOption = transactionRequest.selectedDataPlanType.label,
                            onSelect = { updateDataPlanType.invoke(DataPlanType.getByLabel(it)) },
                            options = computedDataPlanTypes.expressAsString(),
                            enabled = isViewActive,
                        )
                    }
                }
                Product.RESULT_CHECKER -> {
                    enterAmount(
                        value = quantity,
                        onValueChange = { quantity = it },
                        enabled = isViewActive,
                        caption = quantityCaption,
                        isMoney = false,
                        placeholderText = transactionRequest.enterAmountPlaceholderText
                    )
                }
                Product.CABLE -> {
                    enterDeviceInfo(
                        value = deviceNumber,
                        onValueChange = {
                            if (it.length <= Settings.MAX_SMART_CARD_LENGTH) {
                                deviceNumber = it
                            }
                        },
                        enabled = isViewActive,
                        caption = smartCardNumberCaption,
                        placeholderText = smartCardNumberCaption,
                        isLoading = transactionUiState.isLoading,
                        customerName = transactionRequest.customerName,
                        selectedMeterType = transactionRequest.meterType ?: MeterType.PREPAID,
                        product = transactionRequest.product,
                        cableId = transactionRequest.cableId ?: 1,
                        discoId = transactionRequest.discoId ?: 7,
                        validateSmartCardNumber = validateSmartCardNumber,
                        validateMeter = validateMeter,
                    )
                }
                Product.ELECTRICITY -> {
                    item {
                        Spacer(Modifier.height(12.dp))
                        PoshSwitch(
                            selectedOption = transactionRequest.selectedMeterType.title,
                            onSelect = { updateMeterType.invoke(MeterType.getByTitle(it)) },
                            options = MeterType.expressAsStrings(),
                            enabled = isViewActive,
                        )
                    }
                }
                Product.PRINT_CARD -> {
                    item {
                        Spacer(Modifier.height(12.dp))
                        PoshSwitch(
                            selectedOption = transactionRequest.pinResponse?.expressAsString(context).orEmpty(),
                            onSelect = { amountInString ->
                                val decimalAmount =  amountInString.convertMoneyToDouble()
                                val currentNetwork = Network.getByName(transactionRequest.networkName.orEmpty())
                                    ?: Network.MTN
                                updatePinResponse.invoke(
                                loggedInUserState.userConfig.rechargeResponse
                                    ?.getByAmount(
                                        decimalAmount,
                                        currentNetwork
                                    ) ?: PinResponse.Empty
                            ) },
                            options = computedPinResponses.expressAsStrings(context),
                            enabled = isViewActive,
                        )
                    }
                }
                Product.TRANSFER, Product.AIRTIME_SWAP -> {
                    enterAmount(
                        value = amount,
                        onValueChange = { amount = it },
                        enabled = isViewActive,
                        caption = amountCaption,
                        placeholderText = amountCaption
                    )
                }
                Product.REFER_FRIEND -> {
                    item {
                        Spacer(Modifier.height(12.dp))
                    }

                    actionButton(
                        onClick = onFundFromBonus,
                        role = ButtonRole.SECONDARY,
                        enabled = isViewActive,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        label = withdrawFromBonusText,
                    )
                }
                Product.BULK_SMS -> {

                    enterText(
                        value = username,
                        onValueChange = { username = it },
                        enabled = isViewActive,
                        placeholderText = enterSenderNamePlaceholderText,
                        caption = enterSenderNamePlaceholderText,
                    )
                }

                else -> Unit
            }

            when(transactionRequest.product) {
                Product.AIRTIME -> {
                    enterAmount(
                        value = amount,
                        onValueChange = { amount = it },
                        enabled = isViewActive,
                        caption = amountCaption,
                        placeholderText = transactionRequest.enterAmountPlaceholderText
                    )
                }
                Product.DATA -> {
                    selectDataPlan(
                        dataPlans = computedDataPlans,
                        enabled = isViewActive,
                        selectedPlanId = transactionRequest.planId,
                        onSelect = onSelectDataPlan,
                        isLoading = transactionUiState.isLoading == true,
                    )
                }
                Product.RESULT_CHECKER -> {
                    showAmount(
                        value = resultCheckerTotalAmountString,
                        caption = totalAmountCaption
                    )
                }
                Product.CABLE -> {
                    selectCablePlan(
                        cablePlans = computedCablePlans,
                        enabled = isViewActive,
                        selectedPlanId = transactionRequest.planId,
                        onSelect = onSelectCablePlan,
                        isLoading = transactionUiState.isLoading == true,
                    )
                }
                Product.ELECTRICITY -> {
                    enterDeviceInfo(
                        value = deviceNumber,
                        onValueChange = {
                            if (it.length <= Settings.MAX_METER_NUMBER_LENGTH) {
                                deviceNumber = it
                            }
                        },
                        enabled = isViewActive,
                        caption = meterNumberCaption,
                        placeholderText = meterNumberCaption,
                        isLoading = transactionUiState.isLoading,
                        customerName = transactionRequest.customerName,
                        selectedMeterType = transactionRequest.meterType ?: MeterType.PREPAID,
                        product = transactionRequest.product,
                        cableId = transactionRequest.cableId ?: 1,
                        discoId = transactionRequest.discoId ?: 7,
                        validateSmartCardNumber = validateSmartCardNumber,
                        validateMeter = validateMeter,
                    )
                }
                Product.PRINT_CARD -> {
                    enterText(
                        value = nameOnCard,
                        onValueChange = { nameOnCard = it },
                        enabled = isViewActive,
                        caption = nameOnCardCaption,
                        placeholderText = companyNamePlaceholder,
                    )
                }
                Product.AIRTIME_SWAP -> {
                    showAmount(
                        value = airtimeSwapTotalAmountString,
                        caption = totalAmountCaption
                    )
                }
                Product.BULK_SMS -> {

                    enterText(
                        value = message,
                        onValueChange = { message = it },
                        enabled = isViewActive,
                        placeholderText = enterMessageBodyPlaceholderText,
                        caption = enterMessageBodyPlaceholderText,
                        isMultilineText = true,
                    )
                }

                else -> Unit
            }

            when(transactionRequest.product) {
                Product.AIRTIME -> {
                    showAmount(
                        value = airtimeTotalAmountString,
                        caption = totalAmountCaption
                    )
                }
                Product.ELECTRICITY -> {
                    enterAmount(
                        value = amount,
                        onValueChange = { amount = it },
                        enabled = isViewActive,
                        caption = amountCaption,
                        placeholderText = amountCaption,
                    )
                }
                Product.PRINT_CARD -> {
                    enterAmount(
                        value = quantity,
                        onValueChange = { quantity = it },
                        enabled = isViewActive,
                        caption = quantityCaption,
                        isMoney = false,
                        placeholderText = transactionRequest.enterAmountPlaceholderText
                    )
                }

                else -> Unit
            }

            when(transactionRequest.product) {
                Product.PRINT_CARD -> {
                    showAmount(
                        value = cardPrintingTotalAmountString,
                        caption = totalAmountCaption
                    )
                }
                else -> Unit
            }


            item {
                Spacer(Modifier.height(72.dp))
            }

            when(transactionRequest.product) {
                Product.REFER_FRIEND -> Unit
                else -> {
                    actionButton(
                        onClick = onCheckout,
                        enabled = isViewActive && transactionRequest.isValid(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        label = transactionRequest.actionLabel.orEmpty()
                    )

                    item {
                        Spacer(Modifier.height(102.dp))
                    }
                }
            }

        }

        PoshAlertDialog(
            itemsWithIcon = {
                transactionRequest.itemsWithIcon(
                    appUiState.disabledNetworks ?: listOf()
                )
            },
            onDismiss = { onChangeDialogState.invoke(transactionUiState.shouldShowDialog ?: false) },
            selectedItemName = transactionRequest.selectedItemName,
            onChangeItem = onChangeDialogItem,
            title = transactionRequest.dialogTitle,
            visible = transactionUiState.shouldShowDialog == true,
            paddingValues = innerPadding
        )

        if(transactionUiState.error != null) {
            PoshSnackBar(
                message = transactionUiState.error.message.orEmpty(),
                onAction = onHandleThrowable,
                paddingValues = innerPadding
            )

        }

    }

}