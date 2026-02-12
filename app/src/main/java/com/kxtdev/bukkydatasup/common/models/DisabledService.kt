package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product

data class DisabledService(
    val service: String? = null,
    val isDisabled: Boolean? = null
)

data class DisabledNetwork(
    private val networkName: String? = null,
    private val isAirtimeDisabled: Boolean? = null,
    private val isDataDisabled: Boolean? = null,
    private val isDataCardDisabled: Boolean? = null,
    private val isRechargePinDisabled: Boolean? = null,
    private val isVtuDisabled: Boolean? = null,
    private val isShareAndSellDisabled: Boolean? = null,
) {
    fun getNetworkIfDisabledOrNull(product: Product): Network? {
        if(networkName.isNullOrBlank()) return null
        val network = Network.getByName(networkName)
        val disabled = check(product) ?: false
        return if (disabled) network else null
    }

    private fun check(product: Product): Boolean? {
        return when(product) {
            Product.AIRTIME -> isAirtimeDisabled
            Product.DATA -> isDataDisabled
            Product.PRINT_CARD -> isRechargePinDisabled
            else -> null
        }
    }
}

fun List<DisabledNetwork>.getNetworks(product: Product): List<Network> {
    return mapNotNull { networks ->  networks.getNetworkIfDisabledOrNull(product) }
}