package com.kxtdev.bukkydatasup.modules.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationUiState
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshSoftKeyboard
import com.kxtdev.bukkydatasup.ui.design.PoshTextGroup
import com.kxtdev.bukkydatasup.ui.design.exts.PoshBox
import com.kxtdev.bukkydatasup.ui.design.exts.PoshColumn
import com.kxtdev.bukkydatasup.ui.design.exts.PoshRow
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import kotlinx.coroutines.delay

@Composable
fun AuthenticationScreen(
    authState: AuthState,
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    onCreateAccount: () -> Unit,
    authenticationUiState: AuthenticationUiState,
    preferenceUiState: PreferenceUiState,
    updatePasscode: (String) -> Unit,
    onLogout: () -> Unit,
    onHandleThrowable: () -> Unit,
    onEngageBiometrics: () -> Unit,
    updateUsername: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onForgotPassword: () -> Unit,
    onAction: () -> Unit,
    onAlternateAction: () -> Unit,
) {
    PoshScaffold { innerPadding ->

        when(authState) {
            AuthState.SPLASH_SCREEN -> {
                PoshBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(id = PoshIcon.FullLogo),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                    )
                }
            }
            AuthState.INTRODUCTION -> {
                val intro1 = stringResource(R.string.enjoy_cheaper_data)
                val intro2 = stringResource(id = R.string.stay_in_touch_with_loved_ones)
                val intro3 = stringResource(R.string.enjoy_fast_purchases)
                val intro4 = stringResource(R.string.become_richer)
                val introItems = listOf(intro1, intro2, intro3, intro4)

                PoshColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(72.dp))
                    Image(
                        painterResource(id = PoshIcon.Logo),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                    )
                    Spacer(Modifier.weight(1f))
                    PoshTextGroup(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        items = introItems,
                    )
                    Spacer(Modifier.weight(1f))
                    PoshButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        text = stringResource(id = R.string.get_started),
                        enabled = true,
                        onClick = onGetStarted,
                        role = ButtonRole.PRIMARY,
                    )
                    Spacer(Modifier.height(72.dp))
                }
            }
            AuthState.WELCOME -> {
                PoshColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(72.dp))
                    Image(
                        painterResource(id = PoshIcon.Logo),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painterResource(id = PoshIcon.Banner),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.weight(1f))
                    PoshButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        text = stringResource(id = R.string.create_account),
                        enabled = true,
                        onClick = onCreateAccount,
                        role = ButtonRole.PRIMARY,
                    )
                    Spacer(Modifier.height(12.dp))
                    PoshButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        text = stringResource(id = R.string.login),
                        enabled = true,
                        onClick = onLogin,
                        role = ButtonRole.SECONDARY,
                    )
                    Spacer(Modifier.height(72.dp))
                }
            }
            AuthState.QUICK_SIGN_IN -> {
                var isViewActive by remember { mutableStateOf(false) }
                var useBiometrics by remember { mutableStateOf(false) }
                var passcode by remember { mutableStateOf("") }

                LaunchedEffect(preferenceUiState, authState) {
                    useBiometrics = preferenceUiState.shouldEnableBiometrics
                }

                LaunchedEffect(authenticationUiState) {
                    isViewActive = authenticationUiState.isLoading != true &&
                            authenticationUiState.error == null
                }

                LaunchedEffect(passcode) {
                    updatePasscode(passcode)
                    delay(Settings.BIOMETRICS_DELAY)
                    if(passcode.length >= Settings.MAX_TRANSACTION_PIN_LENGTH) passcode = ""
                }

                PoshSoftKeyboard(
                    currentInputText = passcode,
                    enabled = isViewActive,
                    onClick = { passcode = it.orEmpty(); if(it == null) onEngageBiometrics.invoke() },
                    useFingerprint = useBiometrics,
                    label = stringResource(id = R.string.enter_passcode_to_continue),
                    paddingValues = innerPadding,
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    val textStyle = MaterialTheme.typography.titleLarge.copy(
                        MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black
                    )
                    TextButton(onClick = onLogout, enabled = isViewActive) {
                        Text(
                            text = stringResource(id = R.string.logout),
                            style = textStyle,
                            textAlign = TextAlign.End
                        )
                    }
                }

            }
            AuthState.LOGIN -> {
                var isViewActive by remember { mutableStateOf(false) }

                LaunchedEffect(
                    authenticationUiState.isLoading,
                    authenticationUiState.error
                ) {
                    isViewActive = authenticationUiState.isLoading != true &&
                            authenticationUiState.error == null
                }

                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var isPasswordVisible by remember { mutableStateOf(false) }

                val keyboardOptions = KeyboardOptions()
                val keyboardActions = KeyboardActions()

                val contentColor = MaterialTheme.colorScheme.onSurface
                val headerTextStyle = MaterialTheme.typography.titleLarge.copy(contentColor)
                val captionStyle = MaterialTheme.typography.labelLarge.copy(contentColor)

                val bottomTextStyle = MaterialTheme.typography.titleSmall.copy(contentColor)
                val bottomActionTextStyle = MaterialTheme.typography.titleSmall.copy(
                    MaterialTheme.colorScheme.onSurface
                )

                LaunchedEffect(username) {
                    updateUsername(username)
                }
                LaunchedEffect(password) {
                    updatePassword(password)
                }

                val passwordTrailingIcon: @Composable () -> Unit = {
                    if (isPasswordVisible) {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            enabled = isViewActive
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = contentColor
                            )
                        }

                    } else {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            enabled = isViewActive
                        ) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = contentColor
                            )
                        }
                    }
                }

                PoshColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))
                    Image(
                        painterResource(id = PoshIcon.Logo),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                    )
                    Spacer(Modifier.height(12.dp))

                    val headerTextRes: Int = R.string.login

                    val placeholder: @Composable (text: String) -> Unit = { t ->
                        Text(
                            text = t,
                            style = MaterialTheme.typography.bodyLarge.copy(contentColor.copy(0.4f))
                        )
                    }

                    headerTextRes.let { h ->
                        Text(
                            text = stringResource(h),
                            style = headerTextStyle,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                        )
                        Spacer(Modifier.height(12.dp))
                    }


                    Text(
                        text = stringResource(id = R.string.username),
                        style = captionStyle,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(8.dp))
                    PoshOutlinedTextField(
                        value = username,
                        enabled = isViewActive,
                        placeholder = { placeholder.invoke(stringResource(id = R.string.username)) },
                        onValueChange = { username = it },
                        keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Next),
                        keyboardActions = keyboardActions,
                        visualTransformation = VisualTransformation.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = stringResource(id = R.string.password),
                        style = captionStyle,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )
                    Spacer(Modifier.height(8.dp))
                    PoshOutlinedTextField(
                        value = password,
                        enabled = isViewActive,
                        placeholder = { placeholder.invoke(stringResource(id = R.string.password)) },
                        onValueChange = { password = it },
                        keyboardOptions = keyboardOptions.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = keyboardActions,
                        trailingIcon = passwordTrailingIcon,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else
                            PasswordVisualTransformation('*'),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            )
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.forgot_password),
                            style = captionStyle.copy(MaterialTheme.colorScheme.secondary),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                                .clickable(
                                    enabled = isViewActive,
                                    onClick = onForgotPassword,
                                    role = Role.Button,
                                )
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    val buttonTextRes: Int = R.string.login

                    buttonTextRes.let { res ->
                        PoshButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                ),
                            text = stringResource(id = res),
                            enabled = isViewActive,
                            onClick = onAction,
                            role = ButtonRole.PRIMARY,
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    val alternateTextRes: Int = R.string.are_you_a_new_user
                    val alternateActionTextRes: Int = R.string.sign_up

                    PoshRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(alternateTextRes),
                            style = bottomTextStyle,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(alternateActionTextRes),
                            style = bottomActionTextStyle,
                            modifier = Modifier
                                .clickable(
                                    enabled = isViewActive,
                                    role = Role.Button,
                                    onClick = onAlternateAction,
                                )
                        )
                    }

                    Spacer(Modifier.height(72.dp))


                }
            }
            else -> Unit
        }

        if(authenticationUiState.isLoading == true) {
            PoshLoader(loadingMessage = authenticationUiState.loadingMessage)
        }

        if(authenticationUiState.error != null) {
            PoshSnackBar(
                message = authenticationUiState.error.message.orEmpty(),
                onAction = onHandleThrowable,
                paddingValues = innerPadding
            )
        }
    }
}


@Composable
@Preview
private fun AuthenticationScreenPreview() {
    MainAppTheme {
        AuthenticationScreen(
            authState = AuthState.SPLASH_SCREEN,
            onGetStarted = {},
            onLogin = {},
            onCreateAccount = {},
            updatePasscode = {},
            preferenceUiState = PreferenceUiState(),
            authenticationUiState = AuthenticationUiState(),
            onLogout = {},
            onHandleThrowable = {},
            onEngageBiometrics = {},
            updateUsername = {},
            updatePassword = {},
            onForgotPassword = {},
            onAction = {},
            onAlternateAction = {},
        )
    }
}