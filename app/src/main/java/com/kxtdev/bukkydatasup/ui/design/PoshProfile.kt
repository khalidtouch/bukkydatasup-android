package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.models.ProfileItem
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme


@Composable
fun PoshProfile(
    profileItem: ProfileItem?,
    onCopy: (String) -> Unit,
) {
    val containerColor = MaterialTheme.colorScheme.secondary
    val contentColor = MaterialTheme.colorScheme.onSecondary
    val shape = RoundedCornerShape(24.dp)

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    val maxTitleWidth = (maxWidth / 2) - 12.dp
    val maxBodyWidth = (maxWidth / 2) - 12.dp

    val titleStyle = MaterialTheme.typography.titleMedium.copy(contentColor)
    val bodyStyle = MaterialTheme.typography.bodyLarge.copy(contentColor, fontWeight = FontWeight.SemiBold)


    Box(
        Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp
            )
            .background(containerColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 32.dp,
                    bottom = 32.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            profileItem?.username?.let { name ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.username),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = name,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = maxBodyWidth)
                            .clickable(onClick = { onCopy.invoke(name) })
                    )
                }
            }

            profileItem?.isAccountVerified?.let { isVerified ->
                val verificationState = if(isVerified) stringResource(id = R.string.verified) else
                    stringResource(id = R.string.pending)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.account_verification),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = verificationState,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxBodyWidth)
                    )
                }
            }

            profileItem?.email?.let { em ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.email),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = em,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = maxBodyWidth)
                            .clickable(onClick = { onCopy.invoke(em) })
                    )
                }
            }

            profileItem?.isEmailVerified?.let { isVerified ->
                val verificationState = if(isVerified) stringResource(id = R.string.verified) else
                    stringResource(id = R.string.pending)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.email_verification),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = verificationState,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxBodyWidth)
                    )
                }
            }

            profileItem?.fullname?.let { name ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.fullname),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = name,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = maxBodyWidth)
                            .clickable(onClick = { onCopy.invoke(name) })
                    )
                }
            }

            profileItem?.phone?.let { p ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.phone),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = p,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = maxBodyWidth)
                            .clickable(onClick = { onCopy.invoke(p) })
                    )
                }
            }

            profileItem?.userType?.let { type ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.user_type),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = type,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxBodyWidth)
                    )
                }
            }

            profileItem?.address?.let { address ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.address),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = address,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(max = maxBodyWidth)
                            .clickable(onClick = { onCopy.invoke(address) })
                    )
                }
            }

            profileItem?.bonusBalance?.let { balance ->
                val bonusBalance = stringResource(id = R.string.money, balance.toString().toDouble())

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PoshAutoSizeText(
                        text = stringResource(id = R.string.bonus_balance),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        maxTextSize = titleStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxTitleWidth)
                    )
                    Spacer(Modifier.width(12.dp))
                    Spacer(Modifier.weight(1f))
                    PoshAutoSizeText(
                        text = bonusBalance,
                        style = bodyStyle,
                        textAlign = TextAlign.End,
                        maxTextSize = bodyStyle.fontSize,
                        maxLines = 1,
                        modifier = Modifier.widthIn(max = maxBodyWidth)
                    )
                }
            }
        }
    }

}

@Composable
@Preview
private fun PoshProfilePreview() {
    MainAppTheme {
        PoshProfile(
            profileItem = ProfileItem(
                username = "jammy",
                isAccountVerified = true,
                email = "jammy@gmail.com",
                isEmailVerified = true,
                fullname = "James Brown",
                phone = "09039493292",
                userType = "Smart Earner",
                address = "No 1 Kool St, UK",
                bonusBalance = 23.5
            ),
            onCopy = {}
        )
    }
}

