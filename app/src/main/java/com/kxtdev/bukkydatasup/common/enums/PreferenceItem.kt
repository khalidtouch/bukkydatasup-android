package com.kxtdev.bukkydatasup.common.enums


enum class PreferenceItem(val id: Int, val title: String, val expandable: Boolean, val category: PreferenceCategory) {
    ENABLE_TRANSACTION_PIN(1, "Enable Transaction Pin", false, PreferenceCategory.SECURITY),
    RESET_TRANSACTION_PIN(2, "Reset Transaction Pin", false, PreferenceCategory.SECURITY),
    RESET_PASSWORD(3, "Reset Password", false, PreferenceCategory.SECURITY),
    BIOMETRICS(4, "Biometrics", false, PreferenceCategory.SECURITY),
    THEMES(5, "Themes", true, PreferenceCategory.APP_AND_DEVICE),
    UPGRADE_ACCOUNT(6, "Upgrade Account", false, PreferenceCategory.SECURITY),
    VERIFY_ACCOUNT(7, "Verify Account", false, PreferenceCategory.SECURITY),
    ENABLE_PASSCODE(8, "Enable PassCode", false, PreferenceCategory.SECURITY);

    companion object {
        private fun getPreferenceItems(): List<PreferenceItem> {
            return entries
                .filterNot { it == UPGRADE_ACCOUNT || it == VERIFY_ACCOUNT || it == THEMES }
                .toTypedArray()
                .toList()
        }

        fun getPreferenceItemsByCategory(category: PreferenceCategory): List<PreferenceItem> {
            return getPreferenceItems()
                .filter { it.category == category }
        }
    }
}

enum class ChildPreferenceItem(val id: Int, val parentId: Int, val title: String) {
    TRANSACTION_PIN(1, 1, "Enable Transaction Pin"),
    RESET_TRANSACTION_PIN(2, 1, "Reset Transaction Pin"),
    RESET_PASSWORD(3, 1, "Reset Password"),
    AFFILIATE(4, 6, "Affilliate"),
    TOP_USER(5, 6, "TopUser"),
    BIOMETRICS(6, 1, "Biometrics"),
    PASSCODE(7, 1, "Enable PassCode");


    companion object {
        fun getChildren(parentId: Int): List<ChildPreferenceItem> {
            return ChildPreferenceItem.entries.filter { it.parentId == parentId }
        }
    }
}


enum class PreferenceCategory(val id: Int, val title: String) {
    SECURITY(1, "Security"),
    APP_AND_DEVICE(2,"App & Device");

    companion object {
        fun all(): List<PreferenceCategory> {
            return entries
                .toTypedArray()
                .toList()
                .filter { it != APP_AND_DEVICE }
        }
    }
}