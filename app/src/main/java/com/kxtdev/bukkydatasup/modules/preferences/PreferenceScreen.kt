package com.kxtdev.bukkydatasup.modules.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand
import com.kxtdev.bukkydatasup.common.enums.PreferenceCategory
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.models.PreferenceScreenUiState
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState
import com.kxtdev.bukkydatasup.ui.PoshToggleSwitch
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshRebrandedListItem
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshThemeDialog
import com.kxtdev.bukkydatasup.ui.design.PoshToolbarLarge
import com.kxtdev.bukkydatasup.ui.theme.AppHeaderStyle
import com.kxtdev.bukkydatasup.ui.theme.Transparent


@Composable
fun PreferenceScreen(
    onBackPressed: () -> Unit,
    viewDetail: (PreferenceItem) -> Unit,
    onToggleCheckbox: (Boolean, PreferenceItem) -> Unit,
    preferenceUiState: PreferenceUiState,
    preferenceScreenUiState: PreferenceScreenUiState,
    onDismissThemeDialog: () -> Unit,
    onChangeThemeBrand: (ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit,
    onChangeDynamicColorPreference: (Boolean) -> Unit,
) {
    val captionColor = MaterialTheme.colorScheme.onPrimary
    val captionStyle = MaterialTheme.typography.titleMedium.copy(captionColor)
    val contentColor = MaterialTheme.colorScheme.onPrimary
    val textStyle = MaterialTheme.typography.titleMedium.copy(contentColor)


    PoshScaffold(
        toolbar = {
            PoshToolbarLarge(
                title = {
                    val header = stringResource(id = R.string.settings)

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
                        Spacer(Modifier.height(32.dp))
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

        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(innerPadding)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
            }

            val preferenceCategories = PreferenceCategory.all()

            preferenceCategories.forEachIndexed { _, preferenceCategory ->
                val preferenceItems = PreferenceItem.getPreferenceItemsByCategory(preferenceCategory)

                item {
                    Text(
                        text = preferenceCategory.title,
                        style = captionStyle,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(preferenceItems) { preference ->

                    val isChecked: Boolean = when(preference) {
                        PreferenceItem.ENABLE_TRANSACTION_PIN ->
                            preferenceUiState.shouldEnableTransactionPin
                        PreferenceItem.BIOMETRICS ->
                            preferenceUiState.shouldEnableBiometrics
                        PreferenceItem.ENABLE_PASSCODE ->
                                preferenceUiState.shouldEnablePassCode
                        else -> false
                    }


                    var trailingIcon: @Composable (() -> Unit)? = null
                    if(preference.expandable) {
                        trailingIcon = {
                            Icon(
                                painterResource(id = PoshIcon.ArrowForward),
                                contentDescription = null,
                                tint = contentColor,
                            )
                        }
                    }

                    when (preference) {
                        PreferenceItem.ENABLE_TRANSACTION_PIN,
                        PreferenceItem.BIOMETRICS,
                        PreferenceItem.ENABLE_PASSCODE -> {
                            trailingIcon = {
                                PoshToggleSwitch(
                                    checked = isChecked,
                                    enabled = true,
                                    onCheckedChange = { onToggleCheckbox(it, preference) }
                                )
                            }
                        }

                        else -> Unit
                    }

                    PoshRebrandedListItem(
                        enabled = true,
                        onClick = { viewDetail(preference) },
                        title = {
                            Text(
                                text = preference.title,
                                style = textStyle,
                            )
                        },
                        trailingIcon = trailingIcon,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(12.dp))
                }

                item {
                    Spacer(Modifier.height(20.dp))
                }
            }

            item {
                Spacer(Modifier.height(32.dp))
            }
        }

        if(preferenceScreenUiState.shouldShowThemeDialog == true) {
            PoshThemeDialog(
                onDismiss = onDismissThemeDialog,
                themeBrand = preferenceUiState.themeBrand,
                useDynamicColor = preferenceUiState.useDynamicColor,
                darkThemeConfig = preferenceUiState.darkThemeConfig,
                onChangeThemeBrand = onChangeThemeBrand,
                onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                paddingValues = innerPadding
            )
        }
    }
}
