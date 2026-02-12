package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.CashFlow
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.models.HistoryDetailItem
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.common.models.RecentHistoryItem
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.common.utils.formatted
import com.kxtdev.bukkydatasup.ui.theme.Green50
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.Red40
import com.kxtdev.bukkydatasup.ui.theme.Red50
import com.kxtdev.bukkydatasup.ui.theme.Transparent
import java.time.LocalDateTime


@Composable
fun PoshRecentHistoryItem(
    modifier: Modifier = Modifier,
    item: RecentHistoryItem?,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val shape = MaterialTheme.shapes.small
    val borderColor = MaterialTheme.colorScheme.primaryContainer
    val labelColor: Color = if(item?.cashFlow == CashFlow.CREDIT) Green50 else Red50
    val labelStyle = MaterialTheme.typography.titleSmall.copy(labelColor)
    val textColor = MaterialTheme.colorScheme.onTertiary
    val productStyle = MaterialTheme.typography.titleMedium.copy(textColor)
    val timestampStyle = MaterialTheme.typography.labelSmall.copy(textColor)

    if(item == null) {
        val textStyle = MaterialTheme.typography.titleMedium.copy(textColor.copy(0.6f))

        Box(
            modifier = modifier
                .height(80.dp)
                .border(1.dp, borderColor, shape)
                .clip(shape)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.no_recent_activity),
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    } else {

        Box(
            modifier = modifier
                .border(1.dp, borderColor, shape)
                .clip(shape)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item?.product?.icon!!),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(Settings.HISTORY_IMAGE_SIZE)
                )
                Spacer(Modifier.width(12.dp))
                Column(
                    Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.product.title,
                        style = productStyle,
                        textAlign = TextAlign.Start,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.timestamp.formatted() ?: LocalDateTime.MIN.formatted().orEmpty(),
                        style = timestampStyle,
                        textAlign = TextAlign.Start,
                    )
                }
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if(item.cashFlow == CashFlow.CREDIT) "+${item.label}" else "-${item.label}",
                    style = labelStyle,
                    textAlign = TextAlign.End,
                )
            }
        }
    }

}


@Composable
fun PoshHistoryItem(
    modifier: Modifier = Modifier,
    listItem: HistoryListItem?,
    onClick: (Int) -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
) {
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    
    PoshHistoryShimmer(
        isLoading = isLoading,
        itemWidth = maxWidth,
        itemHeight = 120.dp
    ) {
        PoshHistoryItem(
            modifier = modifier,
            listItem = listItem,
            onClick = onClick,
            enabled = enabled,
        )
    }
}

@Composable
private fun PoshHistoryItem(
    modifier: Modifier = Modifier,
    listItem: HistoryListItem?,
    onClick: (Int) -> Unit,
    enabled: Boolean,
) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp - 32.dp
    val maxAmountWidth = maxWidth / 3

    val shape = RoundedCornerShape(24.dp)
    val containerColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onPrimary
    val productStyle = MaterialTheme.typography.bodyLarge.copy(textColor, fontWeight = FontWeight.Bold)
    val recipientStyle = MaterialTheme.typography.bodyMedium.copy(textColor)
    val amountStyle = MaterialTheme.typography.titleMedium.copy(textColor)
    val statusColor = listItem?.status?.color ?: Red40
    val statusStyle = MaterialTheme.typography.labelMedium.copy(statusColor)
    val timestampStyle = MaterialTheme.typography.labelMedium.copy(textColor.copy(0.6f))

    Box(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                enabled = enabled,
                onClick = { onClick.invoke(listItem?.id!!) },
            )
            .background(containerColor, shape)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 12.dp,
                    end = 12.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                listItem?.icon?.let { icon ->
                    Box(
                        Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(id = icon),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(Settings.HISTORY_IMAGE_SIZE)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(
                     Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    PoshAutoSizeText(
                        text = listItem?.timestamp?.formatted().orEmpty(),
                        style = productStyle,
                        maxTextSize = productStyle.fontSize,
                        maxLines = 1,
                        textAlign = TextAlign.Start
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = listItem?.recipient.orEmpty(),
                        style = recipientStyle,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = listItem?.status?.title.orEmpty(),
                        style = statusStyle,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Spacer(Modifier.weight(1f))
                PoshAutoSizeText(
                    text = stringResource(id = R.string.money, listItem?.amount.toString().asMoney()),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    style = amountStyle,
                    maxTextSize = amountStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxAmountWidth)
                )
            }
        }
    }
}


@Composable
fun PoshHistoryDetailItem(
    modifier: Modifier = Modifier,
    onCopy: (String) -> Unit,
    detailItem: HistoryDetailItem?,
    paddingValues: PaddingValues,
) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp - 32.dp
    val maxTitleWidth = (maxWidth / 2) - 48.dp
    val maxBodyWidth = (maxWidth / 2) - 22.dp

    val shape = RectangleShape
    val textColor = MaterialTheme.colorScheme.onTertiary
    val titleStyle = MaterialTheme.typography.titleMedium.copy(textColor, fontWeight = FontWeight.Bold)
    val bodyStyle = MaterialTheme.typography.bodyLarge.copy(textColor, fontWeight = FontWeight.Bold)
    val statusColor = detailItem?.status?.color ?: Red40
    val statusStyle = bodyStyle.copy(statusColor)

    Column(
        modifier
            .background(Transparent, shape)
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(
                start = 16.dp,
                end = 16.dp,
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        detailItem?.recipient?.let { rec ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                val recipientTitle: String = when(detailItem.product) {
                    Product.AIRTIME, Product.DATA, Product.BULK_SMS -> stringResource(id = R.string.phone_number)
                    Product.CABLE -> stringResource(id = R.string.smart_card_number)
                    Product.ELECTRICITY -> stringResource(id = R.string.meter_number)
                    Product.RESULT_CHECKER -> stringResource(id = R.string.exam_number)
                    Product.PRINT_CARD -> stringResource(id = R.string.name_on_card)
                    else -> ""
                }

                PoshAutoSizeText(
                    text = recipientTitle,
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                when(detailItem.product) {
                    Product.WALLET_HISTORY -> Unit
                    else -> {
                        Icon(
                            painterResource(id = PoshIcon.Copy),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.clickable(onClick = { onCopy.invoke(rec) })
                        )
                    }
                }
                PoshAutoSizeText(
                    text = rec,
                    style = bodyStyle,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                        .clickable(
                            onClick = { onCopy.invoke(rec) }
                        ),
                )
            }
        }

        detailItem?.providerName?.let { provider ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                val providerTitle: String = when(detailItem.product) {
                    Product.AIRTIME, Product.DATA, Product.PRINT_CARD -> stringResource(id = R.string.network)
                    Product.CABLE -> stringResource(id = R.string.cable)
                    Product.ELECTRICITY -> stringResource(id = R.string.disco)
                    Product.RESULT_CHECKER -> stringResource(id = R.string.exam)
                    Product.BULK_SMS -> stringResource(id = R.string.sender)
                    else -> ""
                }

                PoshAutoSizeText(
                    text = providerTitle,
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                PoshAutoSizeText(
                    text = provider,
                    style = bodyStyle,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                )
            }
        }

        detailItem?.amount?.let { amt ->
            val amount = stringResource(id = R.string.money, amt.toString().asMoney())

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                PoshAutoSizeText(
                    text = stringResource(id = R.string.amount),
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                PoshAutoSizeText(
                    text = amount,
                    style = bodyStyle,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                )
            }
        }

        detailItem?.status?.let { stat ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                PoshAutoSizeText(
                    text = stringResource(id = R.string.status),
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                PoshAutoSizeText(
                    text = stat.title,
                    style = statusStyle,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    maxTextSize = statusStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                )
            }
        }

        detailItem?.apiResponse?.let { res ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                val responseTitle: String = when(detailItem.product) {
                    Product.AIRTIME -> stringResource(id = R.string.airtime_type)
                    Product.DATA -> stringResource(id = R.string.api_response)
                    Product.CABLE -> stringResource(id = R.string.plan_name)
                    Product.ELECTRICITY -> stringResource(id = R.string.token)
                    Product.RESULT_CHECKER, Product.PRINT_CARD -> stringResource(id = R.string.pins)
                    Product.BULK_SMS -> stringResource(id = R.string.message)
                    else -> ""
                }

                val mOnCopy: (String) -> Unit = { value ->
                    when (detailItem.product) {
                        Product.ELECTRICITY,
                        Product.RESULT_CHECKER,
                        Product.PRINT_CARD -> onCopy.invoke(value)
                        else -> Unit
                    }
                }

                PoshAutoSizeText(
                    text = responseTitle,
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                when (detailItem.product) {
                    Product.ELECTRICITY,
                    Product.RESULT_CHECKER,
                    Product.PRINT_CARD -> {
                        Icon(
                            painterResource(id = PoshIcon.Copy),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.clickable(onClick = { mOnCopy.invoke(res) })
                        )
                    }

                    else -> Unit
                }
                PoshAutoSizeText(
                    text = res,
                    style = bodyStyle,
                    maxLines = 7,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                        .clickable(
                            onClick = { mOnCopy.invoke(res) }
                        )
                )
            }
        }

        detailItem?.reference?.let { ref ->

            val mOnCopy: (String) -> Unit = { value ->
                onCopy.invoke(value)
            }

            val referenceTitle: String = stringResource(id = R.string.reference)

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                PoshAutoSizeText(
                    text = referenceTitle,
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Icon(
                    painterResource(id = PoshIcon.Copy),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.clickable(onClick = { mOnCopy.invoke(ref) })
                )
                PoshAutoSizeText(
                    text = ref,
                    style = bodyStyle,
                    maxLines = 2,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                        .clickable(
                            onClick = { mOnCopy.invoke(ref) }
                        )
                )
            }
        }

        detailItem?.timestamp?.let { time ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                PoshAutoSizeText(
                    text = stringResource(id = R.string.date)  ,
                    style = titleStyle,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    maxTextSize = titleStyle.fontSize,
                    modifier = Modifier.widthIn(max = maxTitleWidth),
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                PoshAutoSizeText(
                    text = time.formatted().orEmpty(),
                    style = bodyStyle,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    maxTextSize = bodyStyle.fontSize,
                    modifier = Modifier
                        .widthIn(max = maxBodyWidth)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Spacer(Modifier.weight(1f))
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(
                bottom = 72.dp,
                end = 16.dp,
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painterResource(id = PoshIcon.Logo),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )
    }
}


@Composable
@Preview
private fun PoshWalletHistoryItemPreview() {
    MainAppTheme {

    }
}


@Composable
@Preview
private fun PoshRecentHistoryItemPreview() {
    MainAppTheme {
        PoshRecentHistoryItem(
            item = RecentHistoryItem(
                product = Product.AIRTIME,
                label = "23498587483789379537493898393373985",
                cashFlow = CashFlow.DEBIT,
                timestamp = LocalDateTime.now()
            ),
            onClick = {},
            enabled = true,
        )
    }
}

@Composable
@Preview
private fun PoshNoRecentHistoryItemPreview() {
    MainAppTheme {
        PoshRecentHistoryItem(
            modifier = Modifier,
            item = null,
            onClick = {},
            enabled = true,
        )
    }
}


@Composable
@Preview
private fun PoshHistoryItemPreview() {
    MainAppTheme {
        PoshHistoryItem(
            listItem = HistoryListItem(
                icon = PoshImage.mtn,
                timestamp = LocalDateTime.now(),
                product = Product.AIRTIME,
                status = Status.SUCCESSFUL,
                amount = 900.34,
                recipient = "08145679898",
                id = 12,
            ),
            onClick = {},
            enabled = true,
        )
    }
}

