package com.kxtdev.bukkydatasup.modules.history

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.common.models.HistoryUiState
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.ui.design.PoshEmptyContent
import com.kxtdev.bukkydatasup.ui.design.PoshFilter
import com.kxtdev.bukkydatasup.ui.design.PoshFilterOptions
import com.kxtdev.bukkydatasup.ui.design.PoshHistoryItem
import com.kxtdev.bukkydatasup.ui.design.PoshPagingNavigationBar
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyleOnPrimary
import com.kxtdev.bukkydatasup.ui.theme.Transparent
import kotlinx.coroutines.delay


@Composable
fun HistoryScreen(
    historyUiState: HistoryUiState,
    onToggleFilterExpansion: (Boolean) -> Unit,
    onSelectHistoryItem: (String?) -> Unit,
    onViewDetails: (id: Int) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    currentPage: Int,
    historyListItems: List<HistoryListItem>?,
    historyListPageCount: Long?,
) {
    var isViewActive by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isSettled by remember { mutableStateOf(false) }

    LaunchedEffect(historyUiState) {
        isViewActive = historyUiState.isLoading != true &&
                historyUiState.error == null
    }
    LaunchedEffect(Unit) {
        delay(Settings.TIME_TO_SETTLE)
        isSettled = true
    }

    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = historyUiState.selectedProduct.title

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 12.dp,
                            ),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(Modifier.height(18.dp))
                        Text(
                            text = header,
                            style = AppHeaderStyleOnPrimary,
                            textAlign = TextAlign.Start
                        )
                        Spacer(Modifier.height(32.dp))
                       Row(
                         Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.Start,
                           verticalAlignment = Alignment.CenterVertically,
                       ) {
                           PoshFilter(
                               isSelected = historyUiState.isFilterExpanded == true,
                               onToggle = onToggleFilterExpansion,
                               enabled = true,
                               selectedItem = historyUiState.selectedProduct.title
                           )
                           Spacer(Modifier.weight(1f))
                           Spacer(Modifier.width(16.dp))
                           PoshPagingNavigationBar(
                               currentPage = currentPage,
                               totalPages = historyListPageCount ?: 1,
                               onPrev = onPrev,
                               onNext = onNext,
                               modifier = Modifier,
                           )
                       }
                        Spacer(Modifier.height(12.dp))
                    }
                },
            )
        }
    ) { innerPadding ->

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Transparent)
                    .padding(innerPadding)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                }

                when {
                    historyListItems?.isEmpty() == true -> {
                        item {
                            historyUiState.getEmptyMessage(context)?.let { msg ->
                                PoshEmptyContent(message = msg)
                            }
                        }
                    }

                    historyUiState.isLoading == true -> {

                        item {
                            repeat(8) {
                                PoshHistoryItem(
                                    modifier = Modifier,
                                    listItem = null,
                                    onClick = {},
                                    enabled = false,
                                    isLoading = true,
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }

                    else -> {
                        items(
                            count = historyListItems?.size ?: 0,
                            key = { key -> historyListItems?.get(key)?.id!! }
                        ) { index ->
                            val historyListItem = historyListItems?.get(index)

                            historyListItem.let { history ->
                                PoshHistoryItem(
                                    modifier = Modifier,
                                    listItem = history,
                                    onClick = onViewDetails,
                                    enabled = true,
                                    isLoading = false,
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }

                }

                item {
                    Spacer(Modifier.height(120.dp))
                }
            }

            PoshFilterOptions(
                isVisible = historyUiState.isFilterExpanded == true,
                selectedItem = historyUiState.selectedProduct.title,
                onSelect = onSelectHistoryItem,
                enabled = true,
                options = Product.getHistoryItemsAsString(),
                topPadding = innerPadding.calculateTopPadding()
            )
        }

    }
}