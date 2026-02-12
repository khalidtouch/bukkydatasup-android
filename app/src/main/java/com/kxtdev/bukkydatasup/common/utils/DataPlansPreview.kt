package com.kxtdev.bukkydatasup.common.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.ui.design.PoshEmptyContent
import com.kxtdev.bukkydatasup.ui.design.PoshTripleRowSelectableSurface


@OptIn(ExperimentalLayoutApi::class)
fun LazyListScope.selectDataPlan(
    dataPlans: List<DataPlanItem>,
    enabled: Boolean,
    selectedPlanId: Int?,
    onSelect: (DataPlanItem) -> Unit,
    isLoading: Boolean,
) {
    item {
        val configuration = LocalConfiguration.current
        val maxWidth = configuration.screenWidthDp.dp - 32.dp
        val maxItemWidth = (maxWidth / 3) - 8.dp
        val maxItemHeight = 200.dp

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when {
                isLoading -> {
                    repeat(9) {
                        PoshTripleRowSelectableSurface(
                            topLabel = "",
                            middleLabel = "",
                            bottomLabel = "",
                            onSelect = {},
                            isSelected = false,
                            enabled = false,
                            isLoading = true,
                            width = maxItemWidth,
                            height = maxItemHeight,
                        )
                    }

                }

                dataPlans.isEmpty() -> {

                    PoshEmptyContent(
                        message = stringResource(R.string.no_data_plans)
                    )

                }

                else -> {
                    dataPlans.forEachIndexed { _, planItem ->
                        val selected = planItem.id == selectedPlanId

                        PoshTripleRowSelectableSurface(
                            topLabel = planItem.validity.orEmpty(),
                            middleLabel = planItem.planSize.orEmpty(),
                            bottomLabel = stringResource(
                                id = R.string.money,
                                planItem.planAmount?.asMoney().orEmpty()
                            ),
                            onSelect = { onSelect.invoke(planItem) },
                            isSelected = selected,
                            enabled = enabled,
                            isLoading = false,
                            width = maxItemWidth,
                            height = maxItemHeight,
                        )
                    }

                }
            }
        }
    }
}