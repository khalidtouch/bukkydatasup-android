package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalRequest
import com.kxtdev.bukkydatasup.network.models.NetworkReferralBonusWithdrawalRequest



fun ReferralBonusWithdrawalRequest.convertToWithdrawalRequest(): NetworkReferralBonusWithdrawalRequest =
    NetworkReferralBonusWithdrawalRequest(
        amount = this.amount.toString(),
    )
