package com.kxtdev.bukkydatasup.common.enums

enum class UserType(val title: String) {
    SmartEarner("Smart Earner"),
    API("API"),
    TopUser("TopUser"),
    Affiliate("Affilliate");

    companion object {
        fun getUserType(title: String?): UserType {
            return when(title) {
                SmartEarner.title -> SmartEarner
                API.title -> API
                TopUser.title -> TopUser
                Affiliate.title -> Affiliate
                else -> SmartEarner
            }
        }
    }
}