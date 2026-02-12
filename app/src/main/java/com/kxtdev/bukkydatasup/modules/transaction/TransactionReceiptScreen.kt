package com.kxtdev.bukkydatasup.modules.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.models.TransactionRequest
import com.kxtdev.bukkydatasup.modules.transaction.vm.TransactionUiState
import com.kxtdev.bukkydatasup.ui.design.PoshHistoryDetailItem
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle

@Composable
fun TransactionReceiptScreen(
    transactionUiState: TransactionUiState,
    transactionRequest: TransactionRequest,
    onCopy: (String) -> Unit,
    onBackPressed: () -> Unit,
    onHandleThrowable: () -> Unit,
) {
    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = stringResource(
                        id = R.string.details,
                        transactionRequest.product?.title.orEmpty()
                    )

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = header,
                            style = AppHeaderStyle,
                            textAlign = TextAlign.Start
                        )
                    }

                },
                navigation = {
                    Box(
                        Modifier.padding(top = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(
                            enabled = true,
                            onClick = onBackPressed
                        ) {
                            Icon(
                                painterResource(PoshIcon.ArrowBack),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        PoshHistoryDetailItem(
            onCopy = onCopy,
            detailItem = transactionUiState.transactionReceiptItem,
            paddingValues = innerPadding
        )

        if(transactionUiState.isLoading == true) {
            PoshLoader(loadingMessage = transactionUiState.loadingMessage)
        }

        if(transactionUiState.error != null) {
            PoshSnackBar(
                message = transactionUiState.error.message.orEmpty(),
                onAction = onHandleThrowable,
                paddingValues = innerPadding
            )
        }
    }


}