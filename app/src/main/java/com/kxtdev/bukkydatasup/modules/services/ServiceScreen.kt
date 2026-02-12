package com.kxtdev.bukkydatasup.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshServiceListItem
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyleOnPrimary
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun ServiceScreen(
    onNavigateToTransactionScreen: (Product) -> Unit,
) {
    PoshScaffold (
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = stringResource(id = R.string.services)

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
                        Spacer(Modifier.height(36.dp))
                        Text(
                            text = header,
                            style = AppHeaderStyleOnPrimary,
                            textAlign = TextAlign.Start
                        )
                        Spacer(Modifier.height(18.dp))
                    }

                }
            )
        }
    ) { innerPadding ->

        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(innerPadding)
                .padding( start = 12.dp, end = 12.dp)
        ) {
            items(Product.getMainServices()) { product ->
                PoshServiceListItem(
                    product = product,
                    enabled = true,
                    onClick = onNavigateToTransactionScreen
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                Spacer(Modifier.height(120.dp))
            }
        }

    }
}