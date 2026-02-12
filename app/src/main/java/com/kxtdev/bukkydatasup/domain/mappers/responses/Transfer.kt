package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.TransferFundsResponse
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkTransferFundsResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import java.time.LocalDateTime


fun NetworkTransferFundsResponse.convertToLocalTransfer(): TransferFundsResponse =
    TransferFundsResponse(
        recipientUsername = this.recipientUsername,
        amount = this.amount.toDouble(),
        id = this.id,
        reference = this.reference,
        status = this.status,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
    )


fun NetworkResult<NetworkTransferFundsResponse>.convertToLocalTransferFunds():
        NetworkResult<TransferFundsResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalTransfer())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalTransfer())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}



