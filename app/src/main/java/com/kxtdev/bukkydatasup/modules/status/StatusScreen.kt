package com.kxtdev.bukkydatasup.modules.status

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.theme.Green40
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.PromoColor
import com.kxtdev.bukkydatasup.ui.theme.Red40


@Composable
fun StatusScreen(
    header: String,
    description: String,
    status: Status,
    onAction: () -> Unit,
    canShowReceipt: Boolean,
) {
    PoshScaffold {
        val background = MaterialTheme.colorScheme.surface
        val contentColor = MaterialTheme.colorScheme.onTertiary
        val descriptionStyle = MaterialTheme.typography.bodyLarge.copy(contentColor)
        val icon = when(status) {
            Status.SUCCESSFUL -> PoshIcon.Tick
            Status.PROCESSING -> PoshIcon.Warning
            else -> PoshIcon.Close
        }
        val tint = when(status){
            Status.SUCCESSFUL -> Green40
            Status.PROCESSING -> PromoColor
            else -> Red40
        }
        val headerStyle = MaterialTheme.typography.displaySmall.copy(tint, fontWeight = FontWeight.Bold)
        val buttonTextRes = if(status == Status.SUCCESSFUL) {
            if(canShowReceipt) R.string.preview else R.string.finish
        } else R.string.cancel

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(background)
                .padding(
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier.weight(1.4f), contentAlignment = Alignment.Center) {
                Icon(
                    painterResource(id = icon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(420.dp)
                        .padding(16.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = if(status == Status.PROCESSING) stringResource(id = R.string.processing) else header,
                textAlign = TextAlign.Center,
                style = headerStyle
            )
            Spacer(Modifier.height(72.dp))
            Text(
                text = if(status == Status.PROCESSING) stringResource(id = R.string.please_wait) else description,
                textAlign = TextAlign.Center,
                style = descriptionStyle
            )
            Spacer(Modifier.height(102.dp))
            PoshButton(text = stringResource(id = buttonTextRes),
                enabled = true,
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    ),
                role = ButtonRole.PRIMARY,
            )
            Spacer(Modifier.weight(0.4f))
        }
    }
}

@Composable
@Preview
private fun StatusScreenPreview() {
    MainAppTheme {
        StatusScreen(
            header = "Success!",
            description = "You have transferrred bala",
            status = Status.SUCCESSFUL,
            onAction = {},
            canShowReceipt = true,
        )
    }
}

@Composable
@Preview
private fun StatusScreenFailurePreview() {
    MainAppTheme {
        StatusScreen(
            header = "Ooops!",
            description = "You have not transferrred bala",
            status = Status.FAILED,
            onAction = {},
            canShowReceipt = true,
        )
    }
}

@Composable
@Preview
private fun StatusScreenPendingPreview() {
    MainAppTheme {
        StatusScreen(
            header = "Hmmmm!",
            description = "You have not transferrred bala",
            status = Status.PROCESSING,
            onAction = {},
            canShowReceipt = true,
        )
    }
}