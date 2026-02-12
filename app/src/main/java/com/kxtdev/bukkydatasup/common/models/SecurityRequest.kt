package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.utils.Settings

data class SecurityRequest(
    val pin1: String? = null,
    val pin2: String? = null,
    val password: String? = null,
    val newPassword1: String? = null,
    val newPassword2: String? = null,
    val fullname: String? = null,
    val bvn: String? = null,
    val nin: String? = null,
    val preferenceItem: PreferenceItem? = null,
) {
    val canVerifyAccount: Boolean get() {
        return when(preferenceItem) {
            PreferenceItem.VERIFY_ACCOUNT -> {
                (
                        (!bvn.isNullOrBlank() && bvn.length >= Settings.BVN_MAX_LENGTH) ||
                                (!nin.isNullOrBlank() && nin.length >= Settings.NIN_MAX_LENGTH)
                ) &&
                        !fullname.isNullOrBlank()
            }
            else -> false
        }
    }
}
