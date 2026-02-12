package com.kxtdev.bukkydatasup.ui.design

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfigDefaults.DARK
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfigDefaults.FOLLOW_SYSTEM
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfigDefaults.LIGHT
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrandDefaults
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews

@Composable
fun PoshThemeDialog(
    onDismiss: () -> Unit,
    themeBrand: ThemeBrand?,
    useDynamicColor: Boolean?,
    darkThemeConfig: DarkThemeConfig?,
    onChangeThemeBrand: (ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit,
    onChangeDynamicColorPreference: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {

    val configuration = LocalConfiguration.current
    val contentColor = MaterialTheme.colorScheme.outline
    val headerStyle = MaterialTheme.typography.titleLarge.copy(contentColor)
    val confirmButtonTextStyle = MaterialTheme.typography.labelLarge.copy(contentColor)

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 72.dp)
            .padding(paddingValues),
        onDismissRequest = { onDismiss.invoke() },
        title = {
            Text(
                text = stringResource(id = R.string.theme),
                style = headerStyle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PoshThemeSettingsPanel(
                    onChangeThemeBrand = onChangeThemeBrand,
                    onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                    onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                    themeBrand = themeBrand,
                    useDynamicColor = useDynamicColor,
                    darkThemeConfig = darkThemeConfig,
                )
            }

        },
        confirmButton = {
            TextButton(onDismiss) {
                Text(
                    text = stringResource(id = R.string.done),
                    style = confirmButtonTextStyle,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        },
    )


}


@Composable
private fun ColumnScope.PoshThemeSettingsPanel(
    themeBrand: ThemeBrand?,
    useDynamicColor: Boolean?,
    darkThemeConfig: DarkThemeConfig?,
    onChangeThemeBrand: (ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit,
    onChangeDynamicColorPreference: (Boolean) -> Unit,

    ) {
    val contentColor = MaterialTheme.colorScheme.outline
    val chooserRowTextStyle = MaterialTheme.typography.bodyLarge.copy(contentColor)
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(contentColor)

    PoshSettingsDialogSectionTitle(
        text = stringResource(id = R.string.theme),
        titleTextStyle
    )
    Column(Modifier.selectableGroup()) {
        PoshSettingsDialogThemeChooserRow(
            text = stringResource(id = R.string.theme_default),
            selected = themeBrand == ThemeBrandDefaults.DEFAULT,
            onClick = { onChangeThemeBrand.invoke (ThemeBrandDefaults.DEFAULT) },
            chooserRowTextStyle
        )

        PoshSettingsDialogThemeChooserRow(
            text = stringResource(id = R.string.theme_android),
            selected = themeBrand == ThemeBrandDefaults.ANDROID,
            onClick = { onChangeThemeBrand.invoke (ThemeBrandDefaults.ANDROID) },
            chooserRowTextStyle
        )
    }

    AnimatedVisibility(visible = themeBrand == ThemeBrandDefaults.DEFAULT && useDynamicColor == true) {
        Column {
            PoshSettingsDialogSectionTitle(
                text = stringResource(R.string.dynamic_color_preference),
                titleTextStyle
            )
            Column(Modifier.selectableGroup()) {
                PoshSettingsDialogThemeChooserRow(
                    text = stringResource(R.string.dynamic_color_yes),
                    selected = useDynamicColor == true,
                    onClick = { onChangeDynamicColorPreference.invoke (true) },
                    chooserRowTextStyle
                )
                PoshSettingsDialogThemeChooserRow(
                    text = stringResource(R.string.dynamic_color_no),
                    selected = useDynamicColor != true,
                    onClick = { onChangeDynamicColorPreference.invoke (false) },
                    chooserRowTextStyle
                )
            }
        }
    }

    PoshSettingsDialogSectionTitle(
        text = stringResource(R.string.dark_mode_preference),
        titleTextStyle
    )
    Column(Modifier.selectableGroup()) {
        PoshSettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_system_default),
            selected = darkThemeConfig == FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig.invoke (FOLLOW_SYSTEM) },
            chooserRowTextStyle
        )
        PoshSettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_light),
            selected = darkThemeConfig == LIGHT,
            onClick = { onChangeDarkThemeConfig.invoke (LIGHT) },
            chooserRowTextStyle
        )
        PoshSettingsDialogThemeChooserRow(
            text = stringResource(R.string.dark_mode_config_dark),
            selected = darkThemeConfig == DARK,
            onClick = { onChangeDarkThemeConfig.invoke (DARK) },
            chooserRowTextStyle
        )
    }
}

@Composable
private fun PoshSettingsDialogSectionTitle(
    text: String,
    textStyle: TextStyle,
) {
    Text(
        text = text,
        style = textStyle,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun PoshSettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    textStyle: TextStyle,
) {
    val radioColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedColor = MaterialTheme.colorScheme.outlineVariant,
    )
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, style = textStyle)
        Spacer(Modifier.weight(1f))
        RadioButton(
            selected = selected,
            onClick = null,
            colors = radioColors,
        )
    }
}


@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


@Composable
@ThemePreviews
private fun PoshThemeDialogPreviews() {
    MainAppTheme {
        PoshThemeDialog(
            onDismiss = {},
            themeBrand = ThemeBrandDefaults.DEFAULT,
            useDynamicColor = null,
            darkThemeConfig = DarkThemeConfig(),
            onChangeThemeBrand = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
            paddingValues = PaddingValues()
        )
    }
}