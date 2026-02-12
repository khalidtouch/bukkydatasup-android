package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsRequest
import com.kxtdev.bukkydatasup.network.models.NetworkVerifyReservedAccountsRequest


fun VerifyReservedAccountsRequest.convertToVerifyReservedAccounts():
        NetworkVerifyReservedAccountsRequest = NetworkVerifyReservedAccountsRequest(
    method = this.method,
    fullname = this.fullname,
    bvn = this.bvn,
    nin = this.nin,
)