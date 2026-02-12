package com.kxtdev.bukkydatasup.common.models


class InitMonnifyCardPaymentRequest(
    val amount: Double,
)

class InitMonnifyCardPaymentResponse(
    val checkoutUrl: String,
)