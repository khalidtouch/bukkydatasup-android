package com.kxtdev.bukkydatasup.modules.history.vm

import androidx.lifecycle.viewModelScope
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.HistoryDetailItem
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.common.models.HistoryUiState
import com.kxtdev.bukkydatasup.common.utils.BaseViewModel
import com.kxtdev.bukkydatasup.common.utils.PaginationConfig
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import com.kxtdev.bukkydatasup.modules.history.HistoryScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: AppRepository
) : BaseViewModel() {

    private val error = MutableStateFlow<Throwable?>(null)
    private val isLoading = MutableStateFlow<Boolean?>(null)
    private val isFilterExpanded = MutableStateFlow<Boolean?>(false)
    private val selectedProduct = MutableStateFlow<Product?>(Product.WALLET_HISTORY)
    private val loadingMessage = MutableStateFlow<String>("")

    private val historyScreenState = getHistoryScreenState(
        isLoading = isLoading,
        isFilterExpanded = isFilterExpanded,
        product = selectedProduct,
        error = error
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = HistoryScreenState(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    private val historyDetailItem = MutableStateFlow<HistoryDetailItem>(HistoryDetailItem())

    private val mPage = MutableStateFlow<Int>(1)

    val currentPage: StateFlow<Int> = currentPage(mPage)
        .stateIn(
            scope = viewModelScope,
            initialValue = 1,
            started = SharingStarted.WhileSubscribed(5_000)
        )


    val airtimeHistoryCached: StateFlow<List<HistoryListItem>> =
        getAirtimeHistoryRecordsCached(currentPage, repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val airtimeHistoryRecordPageCount: StateFlow<Long> =
        getAirtimeHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val dataHistoryCached: StateFlow<List<HistoryListItem>> =
        getDataHistoryRecordsCached(currentPage, repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val dataHistoryRecordPageCount: StateFlow<Long> =
        getDataHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val cableHistoryCached: StateFlow<List<HistoryListItem>> =
        getCableHistoryRecordsCached(currentPage, repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val cableHistoryRecordPageCount: StateFlow<Long> =
        getCableHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val meterHistoryCached: StateFlow<List<HistoryListItem>> =
        getMeterHistoryRecordsCached(currentPage, repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val meterHistoryRecordPageCount: StateFlow<Long> =
        getMeterHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val resultCheckerHistoryCached: StateFlow<List<HistoryListItem>> =
        getResultCheckerHistoryRecordsCached(currentPage, repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val resultCheckerHistoryRecordPageCount: StateFlow<Long> =
        getResultCheckerHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val walletSummaryHistoryCached: StateFlow<List<HistoryListItem>> =
        getWalletSummaryHistoryRecordsCached(currentPage,repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val walletSummaryRecordPageCount: StateFlow<Long> =
        getWalletSummaryHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val printCardHistoryCached: StateFlow<List<HistoryListItem>> =
        getPrintCardHistoryRecordsCached(currentPage,repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val printCardRecordPageCount: StateFlow<Long> =
        getPrintCardHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val bulkSMSHistoryCached: StateFlow<List<HistoryListItem>> =
        getBulkSMSHistoryRecordsCached(currentPage,repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val bulkSMSRecordPageCount: StateFlow<Long> =
        getBulkSMSHistoryRecordPageCount(repository)
            .stateIn(
                scope = viewModelScope,
                initialValue = 0L,
                started = SharingStarted.WhileSubscribed(5_000)
            )


    val historyUiState: StateFlow<HistoryUiState> = getHistoryUiState(
        historyScreenState = historyScreenState,
        historyDetailItem = historyDetailItem,
        loadingMessage = loadingMessage
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = HistoryUiState(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    override fun onHandledThrowable() {
        error.value = null
    }

    fun getAirtimeHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getAirtimeHistoryRecordItem(id).let { detail ->
            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )

        }

        isLoading.value = null
    }


    fun getDataHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getDataHistoryRecordItem(id).let { detail ->
            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    fun getCableHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getCableHistoryRecordItem(id).let { detail ->

            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    fun getResultCheckerHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getResultCheckerHistoryRecordItem(id).let { detail ->
            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    fun getMeterHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getMeterHistoryRecordItem(id).let { detail ->

            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }


    fun getWalletSummaryHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getWalletSummaryHistoryRecordItem(id).let { detail ->

            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    fun onPrev(page: Int) = launch {
        mPage.value = page - 1
    }

    fun onNext(page: Int) = launch {
        mPage.value  = page + 1
    }

    fun resetPage() = launch {
        mPage.value = 1
    }


    fun updateFilterExpansionState(expand: Boolean?) {
        val expanded = expand ?: false
        isFilterExpanded.value = !expanded
    }

    fun selectHistoryProduct(product: Product?) {
        selectedProduct.value = product
    }

    fun getPrintCardHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getPrintCardHistoryRecordItem(id).let { detail ->

            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    fun getBulkSMSHistoryDetailItem(id: Int) = launch(Dispatchers.IO) {
        isLoading.value = true
        loadingMessage.value = HISTORY_LOADING_MESSAGE

        repository.getBulkSMSHistoryRecordItem(id).let { detail ->

            historyDetailItem.value = HistoryDetailItem(
                id = null,
                recipient = detail?.recipient,
                providerName = detail?.providerName,
                amount = detail?.amount,
                paidAmount = detail?.paidAmount,
                status = detail?.status,
                apiResponse = detail?.apiResponse,
                product = detail?.product,
                reference = detail?.reference,
                timestamp = detail?.timestamp
            )
        }

        isLoading.value = null
    }

    companion object {
        const val HISTORY_LOADING_MESSAGE = "...just a minute"
    }
}



@OptIn(FlowPreview::class)
fun currentPage(
    page: StateFlow<Int>,
): Flow<Int> {
    return page
        .debounce(Settings.DEBOUNCE_DELAY)
        .map { state -> state }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getAirtimeHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getAirtimeHistoryCached(PaginationConfig(page))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getAirtimeHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getAirtimeHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getDataHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getDataHistoryCached(PaginationConfig(page))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getDataHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getDataHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getCableHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getCableHistoryCached(PaginationConfig(page))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getCableHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getCableHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getMeterHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getMeterHistoryCached(PaginationConfig(page))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getMeterHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getMeterHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getResultCheckerHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getResultCheckerHistoryCached(PaginationConfig(page))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getResultCheckerHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getResultCheckerHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getWalletSummaryHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getWalletSummaryHistoryCached(PaginationConfig(page))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getWalletSummaryHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getWalletSummaryHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}

private fun getHistoryUiState(
    historyScreenState: StateFlow<HistoryScreenState>,
    historyDetailItem: StateFlow<HistoryDetailItem>,
    loadingMessage: StateFlow<String>,
): Flow<HistoryUiState> {
    return combine(
        historyScreenState,
        historyDetailItem,
        loadingMessage
    ) { screen, detail, msg ->
        HistoryUiState(
            error = screen.error,
            loadingMessage = msg,
            isLoading = screen.isLoading,
            isFilterExpanded = screen.isFilterExpanded,
            product = screen.product,
            historyDetailItem = detail,
        )
    }
}

private fun getHistoryScreenState(
    isLoading: StateFlow<Boolean?>,
    isFilterExpanded: StateFlow<Boolean?>,
    product: StateFlow<Product?>,
    error: StateFlow<Throwable?>,
): Flow<HistoryScreenState> {
    return combine(
        isLoading,
        isFilterExpanded,
        product,
        error
    ) { loading, expanded, prod, err ->
        HistoryScreenState(
            isLoading = loading,
            isFilterExpanded = expanded,
            product = prod,
            error = err,
        )
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getPrintCardHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getPrintCardHistoryCached(PaginationConfig(page))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getPrintCardHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getPrintCardHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun getBulkSMSHistoryRecordsCached(
    currentPage: StateFlow<Int>,
    repository: AppRepository,
): Flow<List<HistoryListItem>> {
    return currentPage.flatMapLatest { page ->
        repository.getBulkSMSHistoryCached(PaginationConfig(page))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun getBulkSMSHistoryRecordPageCount(
    repository: AppRepository
): Flow<Long> {
    return repository.getBulkSMSHistoryRecordCountCached()
        .mapLatest { count -> getTotalPages(count, Settings.PAGING_SIZE.toLong()) }
}