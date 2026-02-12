package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.DisabledNetwork
import com.kxtdev.bukkydatasup.common.models.DisabledService
import com.kxtdev.bukkydatasup.network.models.NetworkDisabledNetwork
import com.kxtdev.bukkydatasup.network.models.NetworkDisabledService
import com.kxtdev.bukkydatasup.network.models.NetworkDisabledServiceResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult

fun NetworkResult<NetworkDisabledServiceResponse>.convertToDisabledServices():
        NetworkResult<List<DisabledService>> {
    return when(this) {
        is NetworkResult.Success -> {
            NetworkResult.success(this.data.services?.map { it.convertToDisabledService()  } ?: listOf())
        }
        is NetworkResult.Failed -> {
            NetworkResult.failed(this.data.services?.map { it.convertToDisabledService()  } ?: listOf())
        }
        is NetworkResult.Error -> {
            NetworkResult.failed(listOf())
        }
    }
}

fun NetworkDisabledService?.convertToDisabledService(): DisabledService {
    return DisabledService(
        service = this?.service,
        isDisabled = this?.isDisabled
    )
}

fun NetworkResult<NetworkDisabledServiceResponse>.convertToDisabledNetworks():
        NetworkResult<List<DisabledNetwork>> {
    return when(this) {
        is NetworkResult.Success -> {
            NetworkResult.success(this.data.networks?.map { it.convertToDisabledNetwork()  } ?: listOf())
        }
        is NetworkResult.Failed -> {
            NetworkResult.failed(this.data.networks?.map { it.convertToDisabledNetwork()  } ?: listOf())
        }
        is NetworkResult.Error -> {
            NetworkResult.failed(listOf())
        }
    }
}

fun NetworkDisabledNetwork?.convertToDisabledNetwork(): DisabledNetwork {
    return DisabledNetwork(
        networkName = this?.networkName,
        isAirtimeDisabled = this?.isAirtimeDisabled,
        isDataDisabled = this?.isDataDisabled,
        isVtuDisabled = this?.isVtuDisabled,
        isDataCardDisabled = this?.isDataCardDisabled,
        isRechargePinDisabled = this?.isRechargePinDisabled,
        isShareAndSellDisabled = this?.isShareAndSellDisabled,
    )
}