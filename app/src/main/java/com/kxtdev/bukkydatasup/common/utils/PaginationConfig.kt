package com.kxtdev.bukkydatasup.common.utils

data class PaginationConfig(
    private val page: Int
) {
    val offset: Int get() {
        return limit * (page - 1)
    }

    val limit: Int get() {
        return Settings.PAGING_SIZE
    }

}