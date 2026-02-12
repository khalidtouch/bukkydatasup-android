package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.FundWithCouponResponse
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.common.models.InitPaystackCardPaymentResponse
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalResponse
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkFundWithCouponResponse
import com.kxtdev.bukkydatasup.network.models.NetworkInitMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.network.models.NetworkInitPaystackCardPaymentResponse
import com.kxtdev.bukkydatasup.network.models.NetworkReferralBonusWithdrawalResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import java.time.LocalDateTime


fun NetworkInitMonnifyCardPaymentResponse.convertToMonnifyCardPayment():
        InitMonnifyCardPaymentResponse = InitMonnifyCardPaymentResponse(
    checkoutUrl = this.checkoutUrl
)

fun NetworkInitPaystackCardPaymentResponse.convertToPaystackCardPayment():
        InitPaystackCardPaymentResponse = InitPaystackCardPaymentResponse(
    checkoutUrl = this.checkoutUrl
)


fun NetworkResult<NetworkInitMonnifyCardPaymentResponse>.convertToMonnifyCardPaymentResponse():
        NetworkResult<InitMonnifyCardPaymentResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToMonnifyCardPayment())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToMonnifyCardPayment())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkResult<NetworkInitPaystackCardPaymentResponse>.convertToPaystackCardPaymentResponse():
        NetworkResult<InitPaystackCardPaymentResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToPaystackCardPayment())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToPaystackCardPayment())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}


fun NetworkFundWithCouponResponse.convertToCouponResponse(): FundWithCouponResponse =
    FundWithCouponResponse(
        id = this.id,
        code = this.code,
        amount = amount.toDouble(),
        status = this.status,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
    )

fun NetworkResult<NetworkFundWithCouponResponse>.convertToLocalFundWithCouponResponse():
        NetworkResult<FundWithCouponResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToCouponResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToCouponResponse())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkReferralBonusWithdrawalResponse.convertToLocalReferralBonusWithdrawalResponse():
        ReferralBonusWithdrawalResponse = ReferralBonusWithdrawalResponse(
    amount = this.amount
)


fun NetworkResult<NetworkReferralBonusWithdrawalResponse>.convertToLocalWithdrawalResponse():
        NetworkResult<ReferralBonusWithdrawalResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalReferralBonusWithdrawalResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalReferralBonusWithdrawalResponse())
        is NetworkResult.Error -> NetworkResult.Error(this.message)
    }
}

