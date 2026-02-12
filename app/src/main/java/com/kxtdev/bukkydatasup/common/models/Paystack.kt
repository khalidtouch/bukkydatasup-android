package com.kxtdev.bukkydatasup.common.models

class InitPaystackCardPaymentRequest(
    val amount: Double,
)

class InitPaystackCardPaymentResponse(
    val checkoutUrl: String,
)