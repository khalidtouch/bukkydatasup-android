package com.kxtdev.bukkydatasup.common.models

data class Advertisement(
    val id: Int? = null,
    private val image: String? = null,
    val description: String? = null
) {
    val formattedImage: String? get() {
        return image?.replace("http://","https://")
    }
}