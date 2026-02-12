package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.MerchantBank
import com.kxtdev.bukkydatasup.ui.theme.Black
import com.kxtdev.bukkydatasup.ui.theme.Blue20
import com.kxtdev.bukkydatasup.ui.theme.Gray10
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.PurplishBlue10
import com.kxtdev.bukkydatasup.ui.theme.Red30
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews
import com.kxtdev.bukkydatasup.ui.theme.White


@Composable
fun RebrandedPoshWalletCard(
    modifier: Modifier = Modifier,
    bank: MerchantBank,
    accountName: String,
    accountNumber: String,
    onCopy: (String) -> Unit,
) {
    val parentShape = RoundedCornerShape(22.dp)
    val baseBackground = MaterialTheme.colorScheme.surface
    val height = 180.dp
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    val contentColor = MaterialTheme.colorScheme.onPrimary

    val topSectionShape = RoundedCornerShape(22.dp, 22.dp, 0.dp, 0.dp)
    val bottomSectionShape = RoundedCornerShape(0.dp, 0.dp, 22.dp, 22.dp)

    Box(
        modifier = modifier
            .height(height)
            .width(maxWidth)
            .background(baseBackground, parentShape),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = modifier
                .matchParentSize()
                .padding(bottom = height / 2 + 4.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    topSectionShape
                ),
            contentAlignment = Alignment.TopCenter
        ) {

            Row(
                Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.virtual_account),
                    style = MaterialTheme.typography.titleSmall.copy(contentColor),
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = bank.title,
                    style = MaterialTheme.typography.titleMedium.copy(contentColor),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .widthIn(max = maxWidth / 2)
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painterResource(id = bank.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
        
        Box(
            modifier = modifier
                .matchParentSize()
                .padding(top = height / 2 + 4.dp)
                .background(
                    Blue20,
                    bottomSectionShape
                ),
            contentAlignment = Alignment.CenterStart
        ) {

            Text(
                accountName,
                style = MaterialTheme.typography.titleMedium.copy(contentColor),
                modifier = Modifier.padding(16.dp)
            )
        }

        Box(
            modifier = modifier
                .padding(end = maxWidth / 2 + 22.dp)
                .clip(CircleShape)
                .background(PurplishBlue10, CircleShape)
                .clickable(
                    enabled = true,
                    onClick = { onCopy(accountNumber) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                accountNumber,
                style = MaterialTheme.typography.titleMedium.copy(contentColor),
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}



@Composable
fun PoshWalletCard(
    bank: MerchantBank,
    accountName: String,
    accountNumber: String,
    onCopy: (String) -> Unit,
    isLoading: Boolean,
) {
    AnimatedVisibility(
        visible = true,
    ) {
        PoshWalletCardShimmer(isLoading = isLoading) {
            PoshCard(
                height = 180.dp,
                content = { animatedHeaderColor: Color ->
                    PoshWallet(
                        bank = bank,
                        accountName = accountName,
                        accountNumber = accountNumber,
                        onCopy = onCopy,
                        width = 300.dp,
                        headerColor = animatedHeaderColor,
                    )
                }
            )
        }
    }
}


@Composable
private fun PoshWallet(
    bank: MerchantBank,
    accountName: String,
    accountNumber: String,
    onCopy: (String) -> Unit,
    width: Dp,
    headerColor: Color
) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val accountNameStyle = MaterialTheme.typography.titleSmall.copy(headerColor)
    val bankTextStyle = MaterialTheme.typography.titleMedium.copy(White)
    val accountNumberStyle = MaterialTheme.typography.titleSmall.copy(White)
    val density = LocalDensity.current
    var maxWidth by remember { mutableStateOf(0.dp) }

    Box(
        Modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .width(width)
                .padding(12.dp)
                .onGloballyPositioned {
                    maxWidth = with(density) { it.size.width.toDp() }
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = stringResource(id = R.string.virtual_account),
                    style = accountNameStyle,
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = bank.title,
                    style = bankTextStyle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .widthIn(max = maxWidth / 2)
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painterResource(id = bank.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(22.dp))
            Box(Modifier
                .background(
                    color = containerColor,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable(
                    enabled = true,
                    onClick = { onCopy(accountNumber) }
                ), contentAlignment = Alignment.Center) {
                Row(
                    Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = accountNumber,
                        style = accountNumberStyle
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painterResource(id = PoshIcon.Copy),
                        contentDescription = null,
                        tint = accountNumberStyle.color
                    )
                }
            }

            Spacer(Modifier.height(22.dp))
            Text(
                text = accountName,
                style = accountNameStyle,
                textAlign = TextAlign.Start,
            )
        }
    }
}


@Composable
private fun PoshCard(
    modifier: Modifier = Modifier,
    height: Dp,
    content: @Composable (textColor: Color) -> Unit
) {
    val animatedBackground = MaterialTheme.colorScheme.tertiaryContainer
    val animatedMinorBackground = MaterialTheme.colorScheme.surfaceTint
    val animatedExpanse = 0.55f
    val animatedTextColor = MaterialTheme.colorScheme.onTertiaryContainer

    val brush = Brush.verticalGradient(colorStops = arrayOf(
        animatedExpanse to animatedMinorBackground,
        (1 - animatedExpanse) to animatedBackground,
    ))
    val shape = MaterialTheme.shapes.medium

    Box(
        modifier
            .background(brush, shape)
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        content.invoke(animatedTextColor)
    }
}


@Composable
fun PoshThumbnail(
    modifier: Modifier = Modifier,
    header: String,
    headerStyle: TextStyle,
    subtitle: String,
    subtitleStyle: TextStyle,
    icon: Int,
    iconBackgroundColor: Color,
    containerColor: Color,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = MaterialTheme.shapes.large,
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
@Preview
private fun PoshCardPreview() {
    MainAppTheme {
        PoshCard(
            height = 180.dp,
            content = {
                Text("PoshCard")
            }
        )
    }
}


@Composable
@Preview
private fun PoshThumbnailPreview() {
    MainAppTheme {
        PoshThumbnail(
            header = "Fund Tips",
            subtitle = "Transfer to any of the account number below to fund your wallet teffbbejfdfdjfbdfb sdbsndb msdbs ssj bssd cjdbcd jdbfjdcd n,dv",
            icon = PoshIcon.Bell,
            headerStyle = MaterialTheme.typography.titleSmall.copy(Black),
            subtitleStyle = MaterialTheme.typography.bodyMedium.copy(Black),
            iconBackgroundColor = Red30,
            containerColor = Blue
        )
    }
}


@Composable
@Preview
private fun PoshWalletPreview() {
    MainAppTheme {
        PoshWallet(
            bank = MerchantBank.WEMA,
            accountName = "Adam Brown",
            accountNumber = "4387837834753",
            onCopy = {},
            width = 300.dp,
            headerColor = Gray10,
        )
    }
}

@Composable
@Preview
private fun PoshWalletInCardPreview() {
    MainAppTheme {

        PoshCard(
            height = 180.dp,
            content = {
                PoshWallet(
                    bank = MerchantBank.WEMA,
                    accountName = "Adam Brown",
                    accountNumber = "43878334753",
                    onCopy = {},
                    width = 300.dp,
                    headerColor = Gray10,
                )
            }
        )
    }
}


@Composable
@Preview
private fun PoshWalletCardPreview() {
    MainAppTheme {
        PoshWalletCard(
            bank = MerchantBank.WEMA,
            accountNumber = "4873846384368",
            accountName = "The boy",
            onCopy = {},
            isLoading = false,
        )
    }
}

@Composable 
@ThemePreviews
private fun RebrandedPoshWalletCardPreview() {
    MainAppTheme {
        RebrandedPoshWalletCard(
            modifier = Modifier,
            bank = MerchantBank.WEMA,
            accountName = "Adam Brown",
            accountNumber = "42454433222",
            onCopy = {  }
        )

    }
}