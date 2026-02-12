package com.kxtdev.bukkydatasup.modules.profile

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshAppVersion
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshProfile
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun ProfileScreen(
    onCopy: (String) -> Unit,
    goToPreferences: () -> Unit,
    onVerifyAccount: () -> Unit,
    onLogout: () -> Unit,
    loggedInUserState: LoggedInUserState,
    onUpgradeAccount: () -> Unit,
) {

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp
    val maxButtonWidth = (maxWidth / 2)

    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    Box(
                        Modifier.background(Transparent, RectangleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Spacer(Modifier.height(18.dp))
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                IconButton(onClick = { goToPreferences.invoke() }) {
                                    Icon(
                                        painterResource(id = PoshIcon.Setting),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                Spacer(Modifier.width(16.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                PoshButton(
                                    text = stringResource(id = R.string.verify_account),
                                    enabled = true,
                                    role = ButtonRole.PRIMARY,
                                    onClick = onVerifyAccount,
                                    modifier = Modifier.widthIn(max = maxButtonWidth)
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(innerPadding)
        ) {
           item {
               PoshProfile(
                   profileItem = loggedInUserState.profile,
                   onCopy = onCopy,
               )

               Spacer(modifier = Modifier.height(12.dp))

               PoshButton(
                   text = stringResource(id = R.string.logout),
                   enabled = true,
                   onClick = onLogout,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(
                           start = 16.dp,
                           end = 16.dp,
                       ),
                   role = ButtonRole.PRIMARY
               )

               Spacer(Modifier.height(32.dp))
               PoshAppVersion()

               Spacer(modifier = Modifier.height(180.dp))
           }
        }

    }

}


