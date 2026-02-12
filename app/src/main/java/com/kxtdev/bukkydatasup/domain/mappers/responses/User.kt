package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.database.models.RoomBank
import com.kxtdev.bukkydatasup.common.database.models.RoomCablePlan
import com.kxtdev.bukkydatasup.common.database.models.RoomDataPlan
import com.kxtdev.bukkydatasup.common.database.models.RoomUser
import com.kxtdev.bukkydatasup.common.models.AdminNumberResponse
import com.kxtdev.bukkydatasup.common.models.BankResponse
import com.kxtdev.bukkydatasup.common.models.Banner
import com.kxtdev.bukkydatasup.common.models.CableMetadata
import com.kxtdev.bukkydatasup.common.models.CablePlanItem
import com.kxtdev.bukkydatasup.common.models.CablePlansResponse
import com.kxtdev.bukkydatasup.common.models.DataPlanCategory
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.common.models.DataPlans
import com.kxtdev.bukkydatasup.common.models.ExamItem
import com.kxtdev.bukkydatasup.common.models.ExamsResponse
import com.kxtdev.bukkydatasup.common.models.NotificationResponse
import com.kxtdev.bukkydatasup.common.models.PercentageItemResponse
import com.kxtdev.bukkydatasup.common.models.PercentageResponse
import com.kxtdev.bukkydatasup.common.models.PinResponse
import com.kxtdev.bukkydatasup.common.models.RechargeResponse
import com.kxtdev.bukkydatasup.common.models.RegisterResponse
import com.kxtdev.bukkydatasup.common.models.ResetPasswordRequest
import com.kxtdev.bukkydatasup.common.models.ResetPasswordResponse
import com.kxtdev.bukkydatasup.common.models.ResetTransactionPinResponse
import com.kxtdev.bukkydatasup.common.models.TopUpPercentageItemResponse
import com.kxtdev.bukkydatasup.common.models.TopUpPercentageResponse
import com.kxtdev.bukkydatasup.common.models.UpgradeUserResponse
import com.kxtdev.bukkydatasup.common.models.UserBankResponse
import com.kxtdev.bukkydatasup.common.models.UserInfoResponse
import com.kxtdev.bukkydatasup.common.models.UserProfile
import com.kxtdev.bukkydatasup.common.models.Verification
import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.network.models.NetworkAdminNumberResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBankResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBannerResponse
import com.kxtdev.bukkydatasup.network.models.NetworkCableMetadata
import com.kxtdev.bukkydatasup.network.models.NetworkCablePlanItem
import com.kxtdev.bukkydatasup.network.models.NetworkCablePlansResponse
import com.kxtdev.bukkydatasup.network.models.NetworkDataPlanCategory
import com.kxtdev.bukkydatasup.network.models.NetworkDataPlanItem
import com.kxtdev.bukkydatasup.network.models.NetworkDataPlans
import com.kxtdev.bukkydatasup.network.models.NetworkExamItem
import com.kxtdev.bukkydatasup.network.models.NetworkExamsResponse
import com.kxtdev.bukkydatasup.network.models.NetworkNotificationResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPercentageItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPercentageResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPinResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResetPasswordRequest
import com.kxtdev.bukkydatasup.network.models.NetworkResetPasswordResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResetTransactionPinResponse
import com.kxtdev.bukkydatasup.network.models.NetworkTopUpPercentageItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkTopUpPercentageResponse
import com.kxtdev.bukkydatasup.network.models.NetworkUpgradeUserResponse
import com.kxtdev.bukkydatasup.network.models.NetworkUserBankResponse
import com.kxtdev.bukkydatasup.network.models.NetworkUserInfoResponse
import com.kxtdev.bukkydatasup.network.models.NetworkUserProfileResponse
import com.kxtdev.bukkydatasup.network.models.NetworkVerificationResponse
import com.kxtdev.bukkydatasup.network.models.NetworkVerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun NetworkUserProfileResponse.toRoomUser(): RoomUser {
    val loggedInUser = this.user
    return RoomUser(
        id = loggedInUser?.id ?: -1,
        email = loggedInUser?.email.orEmpty(),
        username = loggedInUser?.username.orEmpty(),
        fullname = loggedInUser?.fullname,
        pin = loggedInUser?.pin,
        address = loggedInUser?.address,
        phone = loggedInUser?.phone,
        userType = loggedInUser?.userType,
        isEmailVerified = loggedInUser?.isEmailVerified,
        isAccountVerified = loggedInUser?.isAccountVerified,
        passwordHash = loggedInUser?.passwordHash,
        accountBalance = loggedInUser?.accountBalance,
        walletBalance = loggedInUser?.walletBalance,
        bonusBalance = loggedInUser?.bonusBalance,
        refererUsername = loggedInUser?.referrerUsername,
        reservedAccountNumber = loggedInUser?.reservedAccountNumber,
        reservedBankName = loggedInUser?.reservedBankName,
        bvn = loggedInUser?.bvn,
        nin = loggedInUser?.nin,
    )
}

fun NetworkResult<NetworkUserProfileResponse>.convertToRoomUser(): RoomUser? {
    return when (this) {
        is NetworkResult.Success -> this.data.toRoomUser()
        is NetworkResult.Failed -> this.data.toRoomUser()
        is NetworkResult.Error -> null
    }
}

fun NetworkResult<NetworkUserProfileResponse>.convertToLocalUserProfile(): NetworkResult<UserProfile> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                UserProfile(
                    user = this.data.user?.convertNetworkUserToLocalUserInfoResponse(),
                    verification = this.data.verification?.toLocalVerification(),
                    notification = this.data.notification?.convertToNotificationResponse(),
                    percentage = this.data.percentage?.convertToLocalPercentageResponse(),
                    topUpPercentage = this.data.topUpPercentage?.convertToLocalPercentage(),
                    adminNumbers = this.data.adminNumbers?.convertToLocalAdminNumber(),
                    exams = this.data.exams?.convertToLocalExam(),
                    banks = this.data.banks?.convertToBankResponse(),
                    banners = this.data.banners?.toLocalBanners(),
                    dataPlans = this.data.dataPlans?.convertToLocalDataPlans(),
                    cablePlans = this.data.cablePlans?.convertToLocalCablePlans(),
                    supportPhoneNumber = this.data.supportPhoneNumber,
                    groupLink = this.data.groupLink,
                    affiliateUpgradeFee = this.data.affiliateUpgradeFee?.toDouble(),
                    topUserUpgradeFee = this.data.topUserUpgradeFee?.toDouble(),
                    recharge = this.data.recharge?.convertToLocalRechargeResponse(),
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.failed(
                UserProfile(
                    user = this.data.user?.convertNetworkUserToLocalUserInfoResponse(),
                    notification = this.data.notification?.convertToNotificationResponse(),
                    percentage = this.data.percentage?.convertToLocalPercentageResponse(),
                    topUpPercentage = this.data.topUpPercentage?.convertToLocalPercentage(),
                    adminNumbers = this.data.adminNumbers?.convertToLocalAdminNumber(),
                    exams = this.data.exams?.convertToLocalExam(),
                    banks = this.data.banks?.convertToBankResponse(),
                    banners = this.data.banners?.toLocalBanners(),
                    dataPlans = this.data.dataPlans?.convertToLocalDataPlans(),
                    cablePlans = this.data.cablePlans?.convertToLocalCablePlans(),
                    supportPhoneNumber = this.data.supportPhoneNumber,
                    groupLink = this.data.groupLink,
                    affiliateUpgradeFee = this.data.affiliateUpgradeFee?.toDouble(),
                    topUserUpgradeFee = this.data.topUserUpgradeFee?.toDouble(),
                    recharge = this.data.recharge?.convertToLocalRechargeResponse(),
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<UserProfile>(this.message)
        }
    }
}

fun NetworkUserInfoResponse.convertNetworkUserToLocalUserInfoResponse(): UserInfoResponse {
    return UserInfoResponse(
        id = id,
        email = email,
        username = username,
        fullname = fullname,
        pin = pin,
        address = address,
        phone = phone,
        userType = userType,
        isEmailVerified = isEmailVerified,
        isAccountVerified = isAccountVerified,
        passwordHash = passwordHash,
        accountBalance = accountBalance,
        walletBalance = walletBalance,
        bonusBalance = bonusBalance,
        referrerUsername = referrerUsername,
        userBanks = userBanks?.convertNetworkBankToLocalBanks(),
        reservedAccountNumber = reservedAccountNumber,
        reservedBankName = reservedBankName,
    )
}


fun List<NetworkUserBankResponse>.convertNetworkBankToLocalBanks(): List<UserBankResponse> {
    return this.map { it.convertToLocalBankResponse() }
}

fun NetworkUserBankResponse.convertNetworkBankToLocalBank(): UserBankResponse {
    return UserBankResponse(
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountName = accountName,
        trackingReference = trackingReference,
    )
}

fun Flow<RoomUser?>.convertToLocalUserResponseFlow(): Flow<UserInfoResponse?> {
    return this.map { roomUser ->
        roomUser?.let { ru ->
            UserInfoResponse(
                id = ru.id,
                email = ru.email,
                username = ru.username,
                fullname = ru.fullname,
                pin = ru.pin,
                address = ru.address,
                phone = ru.phone,
                userType = ru.userType,
                isEmailVerified = ru.isEmailVerified,
                isAccountVerified = ru.isAccountVerified,
                passwordHash = ru.passwordHash,
                accountBalance = ru.accountBalance,
                walletBalance = ru.walletBalance,
                bonusBalance = ru.bonusBalance,
                referrerUsername = ru.refererUsername,
                reservedAccountNumber = ru.reservedAccountNumber,
            )
        }
    }
}


fun NetworkBannerResponse.toLocalBanner(): Banner {
    return Banner(
        banner = this.banner,
        route = this.route,
    )
}

fun List<NetworkBannerResponse>.toLocalBanners(): List<Banner> {
    return this.map { it.toLocalBanner() }
}

fun NetworkVerificationResponse.toLocalVerification(): Verification {
    return Verification(
        bvnVerified = this.bvnVerified,
        ninVerified = this.ninVerified,
    )
}


fun NetworkCablePlansResponse.convertToLocalCablePlans(): CablePlansResponse {
    return CablePlansResponse(
        gotvPlans = this.gotvPlans.convertToLocalCablePlanItem(),
        dstvPlans = this.dstvPlans.convertToLocalCablePlanItem(),
        startimePlans = this.startimePlans.convertToLocalCablePlanItem(),
        metadata = this.metadata.convertToLocalCableMetadata(),
    )
}

fun List<NetworkCablePlanItem>.convertToLocalCablePlanItem(): List<CablePlanItem> {
    return this.map { plan ->
        CablePlanItem(
            id = plan.id,
            cablePlanId = plan.cablePlanId,
            cableName = plan.cableName,
            cablePackage = plan.cablePackage,
            planAmount = plan.planAmount
        )
    }
}


fun List<NetworkCableMetadata>.convertToLocalCableMetadata(): List<CableMetadata> {
    return this.map { meta ->
        CableMetadata(
            id = meta.id,
            name = meta.name
        )
    }
}

fun NetworkNotificationResponse.convertToNotificationResponse(): NotificationResponse {
    return NotificationResponse(
        message = this.message
    )
}

fun NetworkPercentageResponse.convertToLocalPercentageResponse(): PercentageResponse {
    return PercentageResponse(
        mtn = this.mtn.convertToLocalPercentageItem(),
        glo = this.glo.convertToLocalPercentageItem(),
        nineMobile = this.nineMobile.convertToLocalPercentageItem(),
        airtel = this.airtel.convertToLocalPercentageItem(),
    )
}

fun NetworkPercentageItemResponse.convertToLocalPercentageItem(): PercentageItemResponse {
    return PercentageItemResponse(
        percentage = this.percentage,
        phone = this.phone,
    )
}

fun NetworkTopUpPercentageItemResponse.convertToLocalPercentageItem(): TopUpPercentageItemResponse {
    return TopUpPercentageItemResponse(
        vtu = this.vtu,
        shareNSell = this.shareNSell
    )
}

fun NetworkTopUpPercentageResponse.convertToLocalPercentage(): TopUpPercentageResponse {
    return TopUpPercentageResponse(
        mtn = this.mtn.convertToLocalPercentageItem(),
        glo = this.glo.convertToLocalPercentageItem(),
        nineMobile = this.nineMobile.convertToLocalPercentageItem(),
        airtel = this.airtel.convertToLocalPercentageItem(),
    )
}

fun List<NetworkAdminNumberResponse>.convertToLocalAdminNumber(): List<AdminNumberResponse> {
    return this.map { number ->
        AdminNumberResponse(
            network = number.network,
            phone = number.phone
        )
    }
}

fun NetworkExamsResponse.convertToLocalExam(): ExamsResponse {
    return ExamsResponse(
        waec = this.waec?.convertToLocalExamItem(),
        neco = this.neco?.convertToLocalExamItem(),
        nabteb = this.nabteb?.convertToLocalExamItem()
    )
}

fun NetworkExamItem.convertToLocalExamItem(): ExamItem {
    return ExamItem(amount = this.amount)
}

fun List<NetworkBankResponse>.convertToBankResponse(): List<BankResponse> {
    return map { bank ->
        BankResponse(
            bankName = bank.bankName,
            accountName = bank.accountName,
            accountNumber = bank.accountNumber
        )
    }
}

fun NetworkDataPlanItem.toRoomDataPlan(): RoomDataPlan {
    return RoomDataPlan(
        id = id ?: -1,
        planId = dataPlanId,
        networkId = network,
        planType = planType,
        planNetwork = planNetwork,
        validity = validity,
        planSize = planSize,
        planAmount = planAmount,
        affiliatePrice = affiliatePrice,
        topUserPrice = topUserPrice,
    )
}

fun List<NetworkDataPlanItem>.toRoomDataPlans(): List<RoomDataPlan> {
    return this.map { it.toRoomDataPlan() }
}

fun NetworkDataPlans.convertToRoomDataPlans(): List<RoomDataPlan> {
    return this.parseAll().toRoomDataPlans()
}

fun NetworkResult<NetworkUserProfileResponse>.getRoomDataPlans(): List<RoomDataPlan> {
    return when(this) {
        is NetworkResult.Success -> this.data.dataPlans?.convertToRoomDataPlans() ?: listOf()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun RoomDataPlan.toDataPlan(): DataPlanItem {
    return DataPlanItem(
        id = id,
        dataPlanId = planId,
        network = networkId,
        planType = planType,
        planNetwork = planNetwork,
        validity = validity,
        planSize = planSize,
        planAmount = planAmount,
    )
}

fun Flow<List<RoomDataPlan>>.convertToDataPlans(): Flow<List<DataPlanItem>> {
    return this.map { data -> data.map { rp -> rp.toDataPlan() } }
}

fun NetworkDataPlans.convertToLocalDataPlans(): DataPlans {
    return DataPlans(
        mtnPlan = this.mtnPlan.convertToLocalDataPlanCategory(),
        gloPlan = this.gloPlan.convertToLocalDataPlanCategory(),
        airtelPlan = this.airtelPlan.convertToLocalDataPlanCategory(),
        nineMobilePlan = this.nineMobilePlan.convertToLocalDataPlanCategory(),
    )
}

fun NetworkDataPlanCategory.convertToLocalDataPlanCategory(): DataPlanCategory {
    return DataPlanCategory(
        allItems = this.allItems.convertToLocalDataPlanItem(),
        cgItems = this.cgItems.convertToLocalDataPlanItem(),
        smeItems = this.smeItems.convertToLocalDataPlanItem(),
        giftingItems = this.giftingItems.convertToLocalDataPlanItem(),
        sme2Items = this.sme2Items.convertToLocalDataPlanItem(),
    )
}

fun List<NetworkDataPlanItem>.convertToLocalDataPlanItem(): List<DataPlanItem> {
    return this.map { item ->
        DataPlanItem(
            id = item.id,
            dataPlanId = item.dataPlanId,
            network = item.network,
            planType = item.planType,
            planNetwork = item.planNetwork,
            validity = item.validity,
            planSize = item.planSize,
            planAmount = item.planAmount
        )
    }
}

fun NetworkCablePlanItem.toRoomCablePlan(): RoomCablePlan {
    return RoomCablePlan(
        id = id,
        planId = cablePlanId,
        cableName = cableName,
        packageName = cablePackage,
        planAmount = planAmount,
    )
}

fun List<NetworkCablePlanItem>.toRoomCablePlans(): List<RoomCablePlan> {
    return this.map { it.toRoomCablePlan() }
}

fun NetworkCablePlansResponse.convertToRoomCablePlans(): List<RoomCablePlan> {
    return this.parseAll().toRoomCablePlans()
}

fun NetworkResult<NetworkUserProfileResponse>.getRoomCablePlans(): List<RoomCablePlan> {
    return when(this) {
        is NetworkResult.Success -> this.data.cablePlans?.convertToRoomCablePlans() ?: listOf()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun RoomCablePlan.toCablePlan(): CablePlanItem {
    return CablePlanItem(
        id = id,
        cablePlanId = planId,
        cableName = cableName,
        cablePackage = packageName,
        planAmount = planAmount
    )
}

fun Flow<List<RoomCablePlan>>.convertToCablePlans(): Flow<List<CablePlanItem>> {
    return this.map { data -> data.map { rp -> rp.toCablePlan() } }
}

fun NetworkResult<NetworkUserProfileResponse>.getRoomBanks(): List<RoomBank> {
    return when(this) {
        is NetworkResult.Success -> this.data.user?.userBanks.toRoomBanks() ?: listOf()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun List<NetworkUserBankResponse>?.toRoomBanks(): List<RoomBank>? {
    return this?.map { bank -> bank.toRoomBank() }
}

fun NetworkUserBankResponse.toRoomBank(): RoomBank {
    return RoomBank(
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountName = accountName,
    )
}

fun RoomBank.toBank(): UserBankResponse {
    return UserBankResponse(
        bankCode = bankCode,
        bankName = bankName,
        accountNumber = accountNumber,
        accountName = accountName,
    )
}

fun Flow<List<RoomBank>>.convertToBanks(): Flow<List<UserBankResponse>> {
    return this.map { data -> data.map { bank -> bank.toBank() } }
}

fun NetworkRechargeResponse.convertToLocalRechargeResponse(): RechargeResponse {
    return RechargeResponse(
        mtnItem = this.mtnItem,
        gloItem = this.gloItem,
        airtelItem = this.airtelItem,
        nineMobileItem = this.nineMobileItem,
        mtnPins = this.mtnPins.convertToLocalPins(),
        gloPins = this.gloPins.convertToLocalPins(),
        airtelPins = this.airtelPins.convertToLocalPins(),
        nineMobilePins = this.nineMobilePins.convertToLocalPins(),
    )
}


fun List<NetworkPinResponse>.convertToLocalPins(): List<PinResponse> {
    return map { pin ->
        PinResponse(
            id = pin.id,
            networkName = pin.networkName,
            amount = pin.amount,
            amountToPay = pin.amountToPay,
            affiliatePrice = pin.affiliatePrice,
            topUserPrice = pin.topUserPrice,
            apiPrice = pin.apiPrice,
        )
    }
}


fun NetworkUpgradeUserResponse.convertToLocalUpgradeUser(): UpgradeUserResponse =
    UpgradeUserResponse(
        status = this.status
    )


fun NetworkResult<NetworkUpgradeUserResponse>.convertToLocalUpgradeUserResponse():
        NetworkResult<UpgradeUserResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalUpgradeUser())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalUpgradeUser())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}




fun List<NetworkUserBankResponse>.convertToLocalBankResponses(): List<UserBankResponse> {
    return this.map { it.convertToLocalBankResponse() }
}

fun NetworkUserBankResponse.convertToLocalBankResponse(): UserBankResponse {
    return UserBankResponse(
        bankName = bankName,
        bankCode = bankCode,
        accountNumber = accountNumber,
        accountName = accountName,
        trackingReference = trackingReference,
    )
}


fun NetworkResetTransactionPinResponse.convertToLocalResetTransactionPin():
        ResetTransactionPinResponse = ResetTransactionPinResponse(message)

fun NetworkResult<NetworkResetTransactionPinResponse>.convertToLocalResetTransactionPin():
        NetworkResult<ResetTransactionPinResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResetTransactionPin())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResetTransactionPin())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun ResetPasswordRequest.convertToLocalRequestPassword(): NetworkResetPasswordRequest =
    NetworkResetPasswordRequest(
        oldPassword = oldPassword,
        newPassword1 = newPassword1,
        newPassword2 = newPassword2
    )

fun NetworkResetPasswordResponse.convertToLocalResetPassword(): ResetPasswordResponse =
    ResetPasswordResponse(status)

fun NetworkResult<NetworkResetPasswordResponse>.convertToLocalResetPassword():
        NetworkResult<ResetPasswordResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResetPassword())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResetPassword())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkVerifyReservedAccountsResponse.convertToLocalVerifyReservedAccounts():
        VerifyReservedAccountsResponse = VerifyReservedAccountsResponse(
    status = this.status,
    message = this.message
)

fun NetworkResult<NetworkVerifyReservedAccountsResponse>.convertToLocalVerifyReservedAccountsResponse():
        NetworkResult<VerifyReservedAccountsResponse> =
    when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalVerifyReservedAccounts())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalVerifyReservedAccounts())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }



fun NetworkResult<NetworkRegisterResponse?>.convertToLocalRegisterResponse():
        NetworkResult<RegisterResponse?> =
    when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data?.convertToLocalRegister())
        is NetworkResult.Failed -> NetworkResult.failed(this.data?.convertToLocalRegister())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }



