package com.kxtdev.bukkydatasup.modules.history

import com.kxtdev.bukkydatasup.common.enums.Product

data class HistoryScreenState(
    val isLoading: Boolean? = null,
    val isFilterExpanded: Boolean? = null,
    val product: Product? = null,
    val error: Throwable? = null,
)