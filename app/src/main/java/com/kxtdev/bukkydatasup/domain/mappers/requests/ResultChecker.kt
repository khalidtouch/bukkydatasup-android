package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.ResultCheckerRequest
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerRequest


fun ResultCheckerRequest.convertToResultCheckerRequest():
        NetworkResultCheckerRequest = NetworkResultCheckerRequest(
    examName = this.examName,
    quantity = this.quantity
)
