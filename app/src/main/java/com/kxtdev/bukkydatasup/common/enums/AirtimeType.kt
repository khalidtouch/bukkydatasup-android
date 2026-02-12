package com.kxtdev.bukkydatasup.common.enums

enum class AirtimeType(val title: String) {
    VTU("VTU"),
    SHARE_SELL("Share and Sell");

    companion object {
        fun getByTitle(title: String): AirtimeType {
            return AirtimeType
                .entries
                .toTypedArray()
                .find { it.title.lowercase() == title.lowercase() } ?: VTU
        }

        fun getOptionsAsString(): List<String> {
            return entries
                .toTypedArray()
                .map { it.title }
        }
    }
}