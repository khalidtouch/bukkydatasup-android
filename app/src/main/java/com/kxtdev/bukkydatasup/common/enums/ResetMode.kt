package com.kxtdev.bukkydatasup.common.enums

enum class ResetMode(val title: String) {
    PASSWORD_VERIFICATION("Verify your password"),
    NEW_PIN("Enter new pin"),
    NEW_PIN_AGAIN("Verify new pin"),
    NEW_PASSWORD("Enter new password"),
    NEW_PASSWORD_AGAIN("Verify new password");
}