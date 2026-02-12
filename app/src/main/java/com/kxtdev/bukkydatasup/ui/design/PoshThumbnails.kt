package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.ui.theme.Black
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.PurplishBlue10
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews


@Composable
fun PoshRebrandedWalletThumbnail(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    balance: Double?,
    bonus: Double?,
    fundWallet: () -> Unit,
) {

    val shape = RoundedCornerShape(24.dp)
    val background = PurplishBlue10
    val maxHeight = 150.dp
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 42.dp
    val topSectionHeight = maxHeight - 35.dp

    val onSecondary = MaterialTheme.colorScheme.onSecondary
    val secondary = MaterialTheme.colorScheme.secondary
    val  balanceHeaderStyle = MaterialTheme.typography.titleSmall.copy(onSecondary)

    val mBalance = stringResource(id = R.string.money, balance.toString().asMoney())
    val mBonus = stringResource(id = R.string.money, bonus.toString().asMoney())
    val balanceStyle = MaterialTheme.typography.titleMedium.copy(
        onSecondary, fontWeight = FontWeight.Bold
    )

    Card(
        modifier = modifier.width(maxWidth),
        shape = shape,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = modifier
                .height(maxHeight)
                .background(background, shape),
            contentAlignment = Alignment.TopCenter
        ) {

            Box(
                modifier = modifier
                    .background(secondary,shape)
                    .height(topSectionHeight),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    Modifier.fillMaxWidth()
                        .padding(
                            top = 32.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.wallet_balance),
                            style = balanceHeaderStyle,
                            textAlign = TextAlign.End,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if(visible) mBalance else "********",
                            style = balanceStyle,
                            maxLines = 1,
                        )

                    }

                    Spacer(Modifier.weight(1f))

                    Column(
                        Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.commission),
                            style = balanceHeaderStyle,
                            textAlign = TextAlign.End,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if(visible) mBonus else "********",
                            style = balanceStyle,
                            maxLines = 1,
                        )

                    }
                }

            }

            Box(
                Modifier
                    .matchParentSize()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                PoshButton(
                    modifier = Modifier,
                    text = stringResource(R.string.fund_wallet),
                    enabled = true,
                    role = ButtonRole.PRIMARY,
                    onClick = { fundWallet.invoke() },
                    verticalPadding = 4.dp,
                    horizontalPadding = 16.dp,
                )
            }
        }
    }

}


@Composable
fun PoshTransactionWalletThumbnail(
    modifier: Modifier = Modifier,
    username: String?,
    balance: Double?,
    enabled: Boolean,
    balanceLabel: String = stringResource(id = R.string.wallet_balance),
) {
    val containerColor = MaterialTheme.colorScheme.secondary
    val contentColor = MaterialTheme.colorScheme.onSecondary
    val shape = RoundedCornerShape(24.dp)
    val mUsername = username.orEmpty()
    val mBalance = stringResource(id = R.string.money, balance.toString().asMoney())
    val balanceStyle = MaterialTheme.typography.titleLarge.copy(
        contentColor, fontWeight = FontWeight.Bold
    )
    val  balanceHeaderStyle = MaterialTheme.typography.titleSmall.copy(contentColor)
    val usernameStyle = MaterialTheme.typography.titleMedium.copy(contentColor)
    val overflow = TextOverflow.Ellipsis

    val configuration = LocalConfiguration.current
    var isVisible by remember { mutableStateOf(false) }

    val maxWidth = configuration.screenWidthDp.dp - 32.dp
    val maxUsernameWidth = (maxWidth / 2) - 42.dp
    val maxBalanceWidth = (maxWidth / 2) - 72.dp
    val maxUsernameTextSize = 18.sp
    val maxBalanceTextSize = 22.sp

    val visibilityIcon = if(isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

    Box(
        modifier = modifier
            .background(
                containerColor,
                shape,
            )
            .border(1.dp, MaterialTheme.colorScheme.surfaceContainer, shape),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = PoshIcon.AvatarWithBorder),
                tint = contentColor,
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )
            Spacer(Modifier.width(8.dp))
            PoshAutoSizeText(
                text = mUsername,
                style = usernameStyle,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = overflow,
                maxTextSize = maxUsernameTextSize,
                modifier = Modifier
                    .widthIn(max = maxUsernameWidth)
            )
            Spacer(Modifier.width(12.dp))
            Spacer(Modifier.weight(1f))
            Column(
                Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = balanceLabel,
                    style = balanceHeaderStyle,
                    textAlign = TextAlign.End,
                )
                Spacer(Modifier.height(4.dp))
                PoshAutoSizeText(
                    text = if(isVisible) mBalance else "********",
                    style = balanceStyle,
                    maxLines = 1,
                    overflow = overflow,
                    maxTextSize = maxBalanceTextSize,
                    modifier = Modifier
                        .animateContentSize()
                        .widthIn(max = maxBalanceWidth)
                )
            }
            Spacer(Modifier.width(12.dp))
            IconButton(
                onClick = { isVisible = !isVisible },
                enabled = enabled,
            ) {
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = null,
                    tint = contentColor.copy(0.8f)
                )
            }
        }
    }
}

@Composable
fun PoshNoticeThumbnail(
    modifier: Modifier = Modifier,
    header: String,
    subtitle: String,
    icon: Int,
) {

    val textColor = MaterialTheme.colorScheme.onSecondary
    val headerStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor,
        fontWeight = FontWeight.Bold
    )
    val subtitleStyle = MaterialTheme.typography.bodyMedium.copy(textColor)
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer

    val shape = RoundedCornerShape(25.dp)

    Box(
        modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = shape,
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .background(
                        color = iconBackgroundColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(46.dp)
                        .padding(8.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = containerColor
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(
                Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = header,
                    style = headerStyle,
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = subtitleStyle,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}


@Composable
fun PoshDashboardThumbnail(
    modifier: Modifier = Modifier,
    goToFundWallet: () -> Unit,
    enabled: Boolean,
    walletBalance: String?,
    referralBonus: String?,
    username: String?
) {
    val background = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary
    val shape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 22.dp,
        bottomEnd = 22.dp,
    )
    val usernameStyle = MaterialTheme.typography.headlineSmall.copy(contentColor)

    var isBalanceVisible by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxUsernameWidth = maxWidth / 2
    val maxBalanceWidth = (maxWidth / 2) - 72.dp
    val maxUsernameTextSize = 22.sp
    val maxBalanceTextSize = 22.sp


    Box(
        modifier = modifier
            .background(background, shape),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PoshAutoSizeText(
                    text = username.orEmpty(),
                    style = usernameStyle,
                    textAlign = TextAlign.Start,
                    maxTextSize = maxUsernameTextSize,
                    modifier = Modifier
                        .widthIn(max = maxUsernameWidth)
                )
                Spacer(Modifier.weight(1f))

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(12.dp))

                PoshDashboardValueItem(
                    modifier = Modifier,
                    caption = stringResource(id = R.string.wallet_balance),
                    value = if(isBalanceVisible) walletBalance.orEmpty() else "********",
                    textColor = contentColor,
                    maxTextSize = maxBalanceTextSize,
                    maxWidth = maxBalanceWidth,
                )

                Spacer(Modifier.weight(1f))

                PoshDashboardValueItem(
                    modifier = Modifier,
                    caption = stringResource(id = R.string.referral_balance),
                    value = if(isBalanceVisible) referralBonus.orEmpty() else "*********",
                    textColor = contentColor,
                    isLeftAligned = false,
                    maxTextSize = maxBalanceTextSize,
                    maxWidth = maxBalanceWidth,
                )
                Spacer(Modifier.width(12.dp))
            }

            Spacer(Modifier.height(8.dp))
            PoshButton(
                text = stringResource(id = R.string.fund_wallet),
                enabled = enabled,
                role = ButtonRole.VARIANT,
                onClick = { goToFundWallet.invoke() }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PoshDashboardValueItem(
    modifier: Modifier = Modifier,
    caption: String,
    value: String,
    textColor: Color,
    isLeftAligned: Boolean = true,
    maxTextSize: TextUnit,
    maxWidth: Dp,
) {
    val captionStyle = MaterialTheme.typography.labelMedium.copy(textColor)
    val valueStyle = MaterialTheme.typography.titleMedium.copy(textColor)

    Column(
        modifier = modifier,
        horizontalAlignment = if(isLeftAligned) Alignment.Start else Alignment.End,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = caption,
            style = captionStyle,
            textAlign = if(isLeftAligned) TextAlign.Start else TextAlign.End,
        )
        Spacer(Modifier.height(4.dp))
        PoshAutoSizeText(
            text = value,
            style = valueStyle,
            textAlign = if(isLeftAligned) TextAlign.Start else TextAlign.End,
            maxTextSize = maxTextSize,
            modifier = Modifier
                .widthIn(max = maxWidth)
                .animateContentSize()
        )
    }
}


@Composable
@Preview
private fun PoshDashboardThumbnailPreview() {
    MainAppTheme {
        PoshDashboardThumbnail(
            goToFundWallet = {},
            enabled = true,
            walletBalance = "78000000000000",
            referralBonus = "900000000",
            username = "James Maxwell"
        )
    }
}


@Composable
@Preview
private fun PoshDashboardValueItemPreview() {
    MainAppTheme {
        PoshDashboardValueItem(
            caption = "Balance",
            value = "3400",
            maxTextSize = 23.sp,
            maxWidth = 100.dp,
            textColor = Black,
        )
    }
}

@Composable
@Preview
private fun PoshNoticeThumbnailPreview() {
    MainAppTheme {
        PoshNoticeThumbnail(
            header = "Do not touch 534f efefbe  wsjebdf ef efjebfe fedfbdfbef ef",
            subtitle = "This is extremely wonderful ndmned bsdm emefne bsd mfmdmwsbdmw n",
            icon = PoshIcon.Bell
        )
    }
}

@Composable
@Preview
private fun PoshTransactionWalletThumbnailPreview() {
    MainAppTheme {
        PoshTransactionWalletThumbnail(
            username = "JamesFDDerndjfhdsnsdnkdfd",
            balance = 234.0,
            enabled = true,
        )
    }
}

@Composable
@Preview
private fun PoshTransactionWalletThumbnailPreview2() {
    MainAppTheme {
        PoshTransactionWalletThumbnail(
            username = "James",
            balance = 2433535345334.0,
            enabled = true,
        )
    }
}

@Composable
@ThemePreviews
private fun PoshRebrandedWalletThumbnailPreviews() {
    MainAppTheme {
        PoshRebrandedWalletThumbnail(
            visible = false,
            balance = 234.0,
            bonus = 24.0,
            fundWallet = {}
        )
    }
}