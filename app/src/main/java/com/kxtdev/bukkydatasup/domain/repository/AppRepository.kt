package com.kxtdev.bukkydatasup.domain.repository

import com.kxtdev.bukkydatasup.common.database.daos.AirtimeDao
import com.kxtdev.bukkydatasup.common.database.daos.BankDao
import com.kxtdev.bukkydatasup.common.database.daos.BulkSMSDao
import com.kxtdev.bukkydatasup.common.database.daos.CableDao
import com.kxtdev.bukkydatasup.common.database.daos.CablePlanDao
import com.kxtdev.bukkydatasup.common.database.daos.DataDao
import com.kxtdev.bukkydatasup.common.database.daos.DataPlanDao
import com.kxtdev.bukkydatasup.common.database.daos.MeterDao
import com.kxtdev.bukkydatasup.common.database.daos.PrintCardDao
import com.kxtdev.bukkydatasup.common.database.daos.ResultCheckerDao
import com.kxtdev.bukkydatasup.common.database.daos.UserDao
import com.kxtdev.bukkydatasup.common.database.daos.WalletSummaryDao
import com.kxtdev.bukkydatasup.common.models.Advertisement
import com.kxtdev.bukkydatasup.common.models.AlertNotification
import com.kxtdev.bukkydatasup.common.models.BuyDataRequest
import com.kxtdev.bukkydatasup.common.models.BuyDataResponse
import com.kxtdev.bukkydatasup.common.models.CablePlanItem
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.common.models.DisabledNetwork
import com.kxtdev.bukkydatasup.common.models.DisabledService
import com.kxtdev.bukkydatasup.common.models.FundWithCouponRequest
import com.kxtdev.bukkydatasup.common.models.FundWithCouponResponse
import com.kxtdev.bukkydatasup.common.models.HistoryDetailItem
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentRequest
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.common.models.LoginRequest
import com.kxtdev.bukkydatasup.common.models.LoginResponse
import com.kxtdev.bukkydatasup.common.models.LogoutResponse
import com.kxtdev.bukkydatasup.common.models.PrintCardRequest
import com.kxtdev.bukkydatasup.common.models.PrintCardResponse
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeRequest
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeResponse
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalRequest
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalResponse
import com.kxtdev.bukkydatasup.common.models.RegisterFCMTokenRequest
import com.kxtdev.bukkydatasup.common.models.RegisterFCMTokenResponse
import com.kxtdev.bukkydatasup.common.models.RegisterRequest
import com.kxtdev.bukkydatasup.common.models.RegisterResponse
import com.kxtdev.bukkydatasup.common.models.ResetPasswordRequest
import com.kxtdev.bukkydatasup.common.models.ResetPasswordResponse
import com.kxtdev.bukkydatasup.common.models.ResetTransactionPinResponse
import com.kxtdev.bukkydatasup.common.models.ResultCheckerRequest
import com.kxtdev.bukkydatasup.common.models.ResultCheckerResponse
import com.kxtdev.bukkydatasup.common.models.SendBulkSMSRequest
import com.kxtdev.bukkydatasup.common.models.SendBulkSMSResponse
import com.kxtdev.bukkydatasup.common.models.SubscribeBillRequest
import com.kxtdev.bukkydatasup.common.models.SubscribeBillResponse
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVRequest
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVResponse
import com.kxtdev.bukkydatasup.common.models.TransferFundsRequest
import com.kxtdev.bukkydatasup.common.models.TransferFundsResponse
import com.kxtdev.bukkydatasup.common.models.UpgradeUserResponse
import com.kxtdev.bukkydatasup.common.models.UserBankResponse
import com.kxtdev.bukkydatasup.common.models.UserInfoResponse
import com.kxtdev.bukkydatasup.common.models.UserProfile
import com.kxtdev.bukkydatasup.common.models.ValidateMeterResponse
import com.kxtdev.bukkydatasup.common.models.ValidateSmartCardNumberResponse
import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsRequest
import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.common.utils.PaginationConfig
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.convertToAirtimeHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToAirtimeHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToAirtimeTransactionReceipt
import com.kxtdev.bukkydatasup.common.utils.convertToBillTransactionReceipt
import com.kxtdev.bukkydatasup.common.utils.convertToBulkSMSHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToBulkSMSHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToCableHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToCableHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToCableTransactionReceipt
import com.kxtdev.bukkydatasup.common.utils.convertToDataHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToDataHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToDataTransactionReceipt
import com.kxtdev.bukkydatasup.common.utils.convertToMeterHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToMeterHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToPrintCardHistoryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToPrintCardHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToResultCheckerDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToResultCheckerHistoryListItems
import com.kxtdev.bukkydatasup.common.utils.convertToResultCheckerTransactionReceipt
import com.kxtdev.bukkydatasup.common.utils.convertToWalletSummaryDetailItem
import com.kxtdev.bukkydatasup.common.utils.convertToWalletSummaryHistoryListItems
import com.kxtdev.bukkydatasup.domain.datasource.NetworkDataSource
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertLoginRequestToNetwork
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToBuyDataRequestNetwork
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToCouponRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToFCMRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToLocalSubscribeBillRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToNetworkBulkSMSRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToNetworkInitMonnifyCard
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToNetworkPrintCardRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToNetworkRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToNetworkTransferFundsRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToRechargeAirtimeRequestNetwork
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToResultCheckerRequest
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToSubscribeCableNetwork
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToVerifyReservedAccounts
import com.kxtdev.bukkydatasup.domain.mappers.requests.convertToWithdrawalRequest
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertLoginResponseToLocal
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToAdvertisement
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToAlertNotification
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToBanks
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToBuyDataResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToCablePlans
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToDataPlans
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToDisabledNetworks
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToDisabledServices
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToFCMTokenRes
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalBulkSMS
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalFundWithCouponResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalPrintCardResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalRegisterResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalRequestPassword
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalResetPassword
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalResetTransactionPin
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalSubscribeBillResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalTransferFunds
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalUpgradeUserResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalUserProfile
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalUserResponseFlow
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalValidateMeter
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalVerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLocalWithdrawalResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToLogoutResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToRechargeAirtimeResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToResultCheckerResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToRoomUser
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToSubscribeCableTVResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.convertToValidateSmartCardNumberResponse
import com.kxtdev.bukkydatasup.domain.mappers.responses.getAirtimeHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getBulkSMSHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getCableHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getDataHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getMeterHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getPrintCardHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getPrintCardHistoryTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getResultCheckerTotalPages
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomAirtimeHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomBanks
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomBulkSMSHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomCableHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomCablePlans
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomDataHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomDataPlans
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomMeterHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomResultCheckerHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getRoomWalletSummaryHistoryItems
import com.kxtdev.bukkydatasup.domain.mappers.responses.getWalletSummaryHistoryTotalPages
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AppRepository @Inject constructor(
    private val datasource: NetworkDataSource,
    private val userDao: UserDao,
    private val dataPlanDao: DataPlanDao,
    private val cablePlanDao: CablePlanDao,
    private val bankDao: BankDao,
    private val airtimeDao: AirtimeDao,
    private val dataDao: DataDao,
    private val cableDao: CableDao,
    private val meterDao: MeterDao,
    private val resultCheckerDao: ResultCheckerDao,
    private val walletSummaryDao: WalletSummaryDao,
    private val printCardDao: PrintCardDao,
    private val bulkSMSDao: BulkSMSDao,
) {
    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        return datasource.login(request.convertLoginRequestToNetwork())
            .convertLoginResponseToLocal()
    }

    suspend fun getUserProfile(): NetworkResult<UserProfile> {
        return datasource.getUserProfile().convertToLocalUserProfile()
    }

    fun getUserProfileCached(username: String): Flow<UserInfoResponse?> {
        return userDao.getLoggedInUser(username).convertToLocalUserResponseFlow()
    }

    suspend fun saveUserOrUpdate() {
        datasource.getUserProfile().convertToRoomUser()?.let { user ->
            userDao.clear()
            userDao.saveUserOrUpdate(user)
        }
    }

    suspend fun rechargeAirtime(request: RechargeAirtimeRequest): NetworkResult<RechargeAirtimeResponse> {
        return datasource.rechargeAirtime(request.convertToRechargeAirtimeRequestNetwork())
            .convertToRechargeAirtimeResponse()
    }

    suspend fun saveDataPlansOrUpdate() {
        datasource.getUserProfile().getRoomDataPlans().let { plans ->
            if (plans.isNotEmpty()) {
                dataPlanDao.clear()
                dataPlanDao.insertAll(plans)
            }
        }
    }

    fun getDataPlans(networkId: Int, planType: String): Flow<List<DataPlanItem>> {
        return dataPlanDao.getAll(networkId, planType).convertToDataPlans()
    }

    fun getDataPlans(networkId: Int): Flow<List<DataPlanItem>> {
        return dataPlanDao.getAll(networkId).convertToDataPlans()
    }

    suspend fun buyData(request: BuyDataRequest): NetworkResult<BuyDataResponse> {
        return datasource.buyData(request.convertToBuyDataRequestNetwork())
            .convertToBuyDataResponse()
    }

    suspend fun subscribeCable(request: SubscribeCableTVRequest): NetworkResult<SubscribeCableTVResponse> {
        return datasource.subscribeCableTV(request.convertToSubscribeCableNetwork())
            .convertToSubscribeCableTVResponse()
    }

    suspend fun validateSmartCardNumber(
        cableId: Int,
        smartCardNumber: String
    ): NetworkResult<ValidateSmartCardNumberResponse> {
        return datasource.validateSmartCardNumber(cableId, smartCardNumber)
            .convertToValidateSmartCardNumberResponse()
    }

    suspend fun saveCablePlansOrUpdate() {
        datasource.getUserProfile().getRoomCablePlans().let { plans ->
            if (plans.isNotEmpty()) {
                cablePlanDao.clear()
                cablePlanDao.insertAll(plans)
            }
        }
    }

    fun getCablePlans(cableName: String): Flow<List<CablePlanItem>> {
        return cablePlanDao.getAll(cableName).convertToCablePlans()
    }

    suspend fun saveBanksOrUpdate() {
        datasource.getUserProfile().getRoomBanks().let { banks ->
            if (banks.isNotEmpty()) {
                bankDao.clear()
                bankDao.insertAll(banks)
            }
        }
    }

    fun getBanks(): Flow<List<UserBankResponse>> {
        return bankDao.getAll().convertToBanks()
    }

    suspend fun resetTransactionPin(
        pin1: String,
        pin2: String,
        password: String
    ): NetworkResult<ResetTransactionPinResponse> {
        return datasource.resetTransactionPin(pin1, pin2, password)
            .convertToLocalResetTransactionPin()
    }

    suspend fun resetPassword(
        request: ResetPasswordRequest
    ): NetworkResult<ResetPasswordResponse> {
        return datasource.resetPassword(
            request.convertToLocalRequestPassword()
        ).convertToLocalResetPassword()
    }

    suspend fun fundWithCoupon(
        request: FundWithCouponRequest
    ): NetworkResult<FundWithCouponResponse> {
        return datasource.fundWithCoupon(
            request.convertToCouponRequest()
        ).convertToLocalFundWithCouponResponse()
    }

    suspend fun transferFunds(
        request: TransferFundsRequest
    ): NetworkResult<TransferFundsResponse> {
        return datasource.transferFunds(
            request.convertToNetworkTransferFundsRequest()
        ).convertToLocalTransferFunds()
    }

    suspend fun validateMeter(
        discoId: Int,
        meterNumber: String,
        meterType: String
    ): NetworkResult<ValidateMeterResponse> {
        return datasource.validateMeter(
            discoId,
            meterNumber,
            meterType
        ).convertToLocalValidateMeter()
    }

    suspend fun subscribeBill(
        request: SubscribeBillRequest
    ): NetworkResult<SubscribeBillResponse> {
        return datasource.subscribeBill(
            request.convertToLocalSubscribeBillRequest()
        ).convertToLocalSubscribeBillResponse()
    }

    suspend fun checkResult(
        request: ResultCheckerRequest
    ): NetworkResult<ResultCheckerResponse> {
        return datasource.checkResult(
            request.convertToResultCheckerRequest()
        ).convertToResultCheckerResponse()
    }

    suspend fun printCard(
        request: PrintCardRequest
    ): NetworkResult<PrintCardResponse> {
        return datasource.printCard(
            request.convertToNetworkPrintCardRequest()
        ).convertToLocalPrintCardResponse()
    }

    suspend fun upgradeUser(newPackage: String): NetworkResult<UpgradeUserResponse> {
        return datasource.upgradeUser(newPackage)
            .convertToLocalUpgradeUserResponse()
    }

    suspend fun logout(): NetworkResult<LogoutResponse> {
        return datasource.logout()
            .convertToLogoutResponse()
    }

    suspend fun register(request: RegisterRequest): NetworkResult<RegisterResponse> {
        return datasource.register(
            request.convertToNetworkRequest()
        ).convertToLocalRegisterResponse()
    }


    suspend fun registerFCMToken(request: RegisterFCMTokenRequest):
            NetworkResult<RegisterFCMTokenResponse> {
        return datasource.registerFCMToken(
            request.convertToFCMRequest()
        ).convertToFCMTokenRes()
    }

    suspend fun initMonnifyCardPayment(request: InitMonnifyCardPaymentRequest):
            NetworkResult<InitMonnifyCardPaymentResponse> {
        return datasource.initMonnifyCardPayment(
            request.convertToNetworkInitMonnifyCard()
        ).convertToMonnifyCardPaymentResponse()
    }


    suspend fun verifyReservedAccounts(request: VerifyReservedAccountsRequest):
            NetworkResult<VerifyReservedAccountsResponse> {
        return datasource.verifyReservedAccounts(
            request.convertToVerifyReservedAccounts()
        ).convertToLocalVerifyReservedAccountsResponse()
    }

    suspend fun withdrawFromReferralBonus(request: ReferralBonusWithdrawalRequest):
            NetworkResult<ReferralBonusWithdrawalResponse> {
        return datasource.withdrawFromReferralBonus(
            request.convertToWithdrawalRequest()
        ).convertToLocalWithdrawalResponse()
    }

    suspend fun saveAirtimeHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        airtimeDao.clear()

        do {
            val response = datasource.getAirtimeHistory(currentPage)
            totalPages = response.getAirtimeHistoryTotalPages()
            response.getRoomAirtimeHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    airtimeDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getAirtimeHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return airtimeDao.getHistoryRecords(config.limit, config.offset)
            .convertToAirtimeHistoryListItems()
    }


    fun getAirtimeHistoryRecordCountCached(): Flow<Long> {
        return airtimeDao.getCount()
    }

    suspend fun getAirtimeHistoryRecordItem(id: Int): HistoryDetailItem? {
        return airtimeDao.getHistoryRecordItem(id)
            .convertToAirtimeHistoryDetailItem()
    }


    suspend fun saveDataHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        dataDao.clear()

        do {
            val response = datasource.getDataHistory(currentPage)
            totalPages = response.getDataHistoryTotalPages()
            response.getRoomDataHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    dataDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)

    }

    fun getDataHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return dataDao.getHistoryRecords(config.limit, config.offset)
            .convertToDataHistoryListItems()
    }

    fun getDataHistoryRecordCountCached(): Flow<Long> {
        return dataDao.getCount()
    }

    suspend fun getDataHistoryRecordItem(id: Int): HistoryDetailItem? {
        return dataDao.getHistoryRecordItem(id)
            .convertToDataHistoryDetailItem()
    }

    suspend fun saveCableHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        cableDao.clear()

        do {
            val response = datasource.getCableHistory(currentPage)
            totalPages = response.getCableHistoryTotalPages()
            response.getRoomCableHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    cableDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)

    }

    fun getCableHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return cableDao.getHistoryRecords(config.limit, config.offset)
            .convertToCableHistoryListItems()
    }

    fun getCableHistoryRecordCountCached(): Flow<Long> {
        return cableDao.getCount()
    }

    suspend fun getCableHistoryRecordItem(id: Int): HistoryDetailItem? {
        return cableDao.getHistoryRecordItem(id)
            .convertToCableHistoryDetailItem()
    }

    suspend fun saveMeterHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        meterDao.clear()

        do {
            val response = datasource.getBillPaymentHistory(currentPage)
            totalPages = response.getMeterHistoryTotalPages()
            response.getRoomMeterHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    meterDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getMeterHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return meterDao.getHistoryRecords(config.limit, config.offset)
            .convertToMeterHistoryListItems()
    }

    fun getMeterHistoryRecordCountCached(): Flow<Long> {
        return meterDao.getCount()
    }

    suspend fun getMeterHistoryRecordItem(id: Int): HistoryDetailItem? {
        return meterDao.getHistoryRecordItem(id)
            .convertToMeterHistoryDetailItem()
    }


    suspend fun saveResultCheckerHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        resultCheckerDao.clear()

        do {
            val response = datasource.getResultCheckerHistory(currentPage)
            totalPages = response.getResultCheckerTotalPages()
            response.getRoomResultCheckerHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    resultCheckerDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getResultCheckerHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return resultCheckerDao.getHistoryRecords(config.limit, config.offset)
            .convertToResultCheckerHistoryListItems()
    }

    fun getResultCheckerHistoryRecordCountCached(): Flow<Long> {
        return resultCheckerDao.getCount()
    }

    suspend fun getResultCheckerHistoryRecordItem(id: Int): HistoryDetailItem? {
        return resultCheckerDao.getHistoryRecordItem(id)
            .convertToResultCheckerDetailItem()
    }

    suspend fun saveWalletSummaryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        walletSummaryDao.clear()

        do {
            val response = datasource.getWalletSummaryHistory(currentPage)
            totalPages = response.getWalletSummaryHistoryTotalPages()
            response.getRoomWalletSummaryHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    walletSummaryDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getWalletSummaryHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return walletSummaryDao.getHistoryRecords(config.limit, config.offset)
            .convertToWalletSummaryHistoryListItems()
    }

    fun getWalletSummaryHistoryRecordCountCached(): Flow<Long> {
        return walletSummaryDao.getCount()
    }

    suspend fun getWalletSummaryHistoryRecordItem(id: Int): HistoryDetailItem? {
        return walletSummaryDao.getHistoryRecordItem(id)
            .convertToWalletSummaryDetailItem()
    }

    suspend fun sendBulkSMS(request: SendBulkSMSRequest): NetworkResult<SendBulkSMSResponse> {
        return datasource.sendBulkSMS(
            request.convertToNetworkBulkSMSRequest()
        ).convertToLocalBulkSMS()
    }


    suspend fun savePrintCardHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        printCardDao.clear()

        do {
            val response = datasource.getPrintCardHistory(currentPage)
            totalPages = response.getPrintCardHistoryTotalPages()
            response.getPrintCardHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    printCardDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getPrintCardHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return printCardDao.getHistoryRecords(config.limit, config.offset)
            .convertToPrintCardHistoryListItems()
    }

    fun getPrintCardHistoryRecordCountCached(): Flow<Long> {
        return printCardDao.getCount()
    }

    suspend fun getPrintCardHistoryRecordItem(id: Int): HistoryDetailItem? {
        return printCardDao.getHistoryRecordItem(id)
            .convertToPrintCardHistoryDetailItem()
    }

    suspend fun saveBulkSMSHistoryOrUpdate() {
        var totalPages: Long = 1L
        var currentPage = 1

        bulkSMSDao.clear()

        do {
            val response = datasource.getBulkSMSHistory(currentPage)
            totalPages = response.getBulkSMSHistoryTotalPages()
            response.getRoomBulkSMSHistoryItems().let { items ->
                if (items.isNotEmpty()) {
                    bulkSMSDao.insertAll(items)
                }
            }
            delay(Settings.HISTORY_PAGING_DELAY)
            currentPage++
        } while (currentPage <= totalPages)
    }

    fun getBulkSMSHistoryCached(config: PaginationConfig): Flow<List<HistoryListItem>> {
        return bulkSMSDao.getHistoryRecords(config.limit, config.offset)
            .convertToBulkSMSHistoryListItems()
    }

    fun getBulkSMSHistoryRecordCountCached(): Flow<Long> {
        return bulkSMSDao.getCount()
    }

    suspend fun getBulkSMSHistoryRecordItem(id: Int): HistoryDetailItem? {
        return bulkSMSDao.getHistoryRecordItem(id)
            .convertToBulkSMSHistoryDetailItem()
    }

    suspend fun clearUsers() {
        userDao.clear()
    }

    suspend fun clearDataPlans() {
        dataPlanDao.clear()
    }

    suspend fun clearCablePlans() {
        cablePlanDao.clear()
    }

    suspend fun clearBanks() {
        bankDao.clear()
    }

    suspend fun clearAirtime() {
        airtimeDao.clear()
    }

    suspend fun clearData() {
        dataDao.clear()
    }

    suspend fun clearCable() {
        cableDao.clear()
    }

    suspend fun clearMeter() {
        meterDao.clear()
    }

    suspend fun clearResultChecker() {
        resultCheckerDao.clear()
    }

    suspend fun clearWalletSummary() {
        walletSummaryDao.clear()
    }

    suspend fun clearPrintCard() {
        printCardDao.clear()
    }

    suspend fun clearBulkSMS() {
        bulkSMSDao.clear()
    }

    suspend fun getDisabledServices(): NetworkResult<List<DisabledService>> {
        return datasource.getDisabledService().convertToDisabledServices()
    }

    suspend fun getAlertNotification(): NetworkResult<AlertNotification> {
        return datasource.getAlertNotification()
            .convertToAlertNotification()
    }

    suspend fun getDisabledNetworks(): NetworkResult<List<DisabledNetwork>> {
        return datasource.getDisabledService().convertToDisabledNetworks()
    }

    suspend fun getDataReceipt(id: Int): HistoryDetailItem? {
        return datasource.getDataReceipt(id).convertToDataTransactionReceipt()
    }

    suspend fun getAirtimeReceipt(id: Int): HistoryDetailItem? {
        return datasource.getAirtimeReceipt(id).convertToAirtimeTransactionReceipt()
    }

    suspend fun getCableReceipt(id: Int): HistoryDetailItem? {
        return datasource.getCableReceipt(id).convertToCableTransactionReceipt()
    }

    suspend fun getBillReceipt(id: Int): HistoryDetailItem? {
        return datasource.getBillReceipt(id).convertToBillTransactionReceipt()
    }

    suspend fun getResultCheckerReceipt(id: Int): HistoryDetailItem? {
        return datasource.getResultCheckerReceipt(id).convertToResultCheckerTransactionReceipt()
    }

    suspend fun getAds(): List<Advertisement> {
        return datasource.getAds().convertToAdvertisement()
    }

}



