package com.kxtdev.bukkydatasup.common.enums

enum class MeterType(val title: String) {
    PREPAID("Prepaid"),
    POSTPAID("Postpaid");

    companion object {
        fun getByTitle(title: String): MeterType {
            return MeterType
                .entries
                .toTypedArray()
                .find { it.title.lowercase() == title.lowercase() } ?: MeterType.PREPAID
        }
        fun expressAsStrings(): List<String> {
            return entries
                .toTypedArray()
                .map { it.title }
        }
    }
}