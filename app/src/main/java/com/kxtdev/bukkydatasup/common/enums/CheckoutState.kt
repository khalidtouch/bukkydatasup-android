package com.kxtdev.bukkydatasup.common.enums

enum class CheckoutState {
    INFORMATION, SECURITY;

    val prevState: CheckoutState get() {
        return INFORMATION
    }

    val nextState: CheckoutState get() {
        return SECURITY
    }
}