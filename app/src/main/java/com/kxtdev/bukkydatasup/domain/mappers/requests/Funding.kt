package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.FundWithCouponRequest
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentRequest
import com.kxtdev.bukkydatasup.common.models.InitPaystackCardPaymentRequest
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalRequest
import com.kxtdev.bukkydatasup.network.models.NetworkFundWithCouponRequest
import com.kxtdev.bukkydatasup.network.models.NetworkInitMonnifyCardPaymentRequest
import com.kxtdev.bukkydatasup.network.models.NetworkInitPaystackCardPaymentRequest
import com.kxtdev.bukkydatasup.network.models.NetworkReferralBonusWithdrawalRequest



fun InitMonnifyCardPaymentRequest.convertToNetworkInitMonnifyCard():
        NetworkInitMonnifyCardPaymentRequest = NetworkInitMonnifyCardPaymentRequest(
    amount = this.amount.toLong()
)


fun InitPaystackCardPaymentRequest.convertToNetworkInitPaystackCard():
        NetworkInitPaystackCardPaymentRequest = NetworkInitPaystackCardPaymentRequest(
    amount = this.amount.toLong()
)


fun FundWithCouponRequest.convertToCouponRequest(): NetworkFundWithCouponRequest =
    NetworkFundWithCouponRequest(code)
