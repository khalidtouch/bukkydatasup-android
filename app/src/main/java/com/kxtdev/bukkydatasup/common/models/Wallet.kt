package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime


class WalletSummaryHistoryItem(
    val id: Int,
    val reference: String,
    val product: String,
    val amount: Double,
    val balanceBefore: Double,
    val balanceAfter: Double,
    val dateCreated: LocalDateTime,
)

data class WalletSummaryHistory(
    val count: Long,
    val next: String?,
    val prev: String?,
    val result: List<WalletSummaryHistoryItem>
)


data class ReferralBonusWithdrawalRequest(
    val amount: Double,
)

data class ReferralBonusWithdrawalResponse(
    val amount: String
)