package com.kxtdev.bukkydatasup.modules.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.models.AuthenticationRequest
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationUiState
import com.kxtdev.bukkydatasup.modules.authentication.vm.AuthenticationViewModel
import com.kxtdev.bukkydatasup.ui.design.ButtonRole
import com.kxtdev.bukkydatasup.ui.design.PoshButton
import com.kxtdev.bukkydatasup.ui.design.PoshCheckboxWithText
import com.kxtdev.bukkydatasup.ui.design.PoshCircularTabGroup
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshLoader
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField
import com.kxtdev.bukkydatasup.ui.design.PoshScaffold
import com.kxtdev.bukkydatasup.ui.design.PoshSnackBar
import com.kxtdev.bukkydatasup.ui.design.PoshToolbar
import com.kxtdev.bukkydatasup.ui.design.exts.PoshColumn
import com.kxtdev.bukkydatasup.ui.design.exts.PoshRow

@Composable
fun AuthenticationDetailScreen(
    authState: AuthState,
    onBackPressed: () -> Unit,
    onForgotPassword: () -> Unit,
    onAction: () -> Unit,
    onAlternateAction: () -> Unit,
    currentAuthenticationPage: Int,
    onTermsAndPrivacyClicked: () -> Unit,
    onCheckedTermsAndPrivacyPolicy: (Boolean) -> Unit,
    authenticationUiState: AuthenticationUiState,
    onHandleThrowable: () -> Unit,
    authenticationRequest: AuthenticationRequest,
    updateUsername: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updateConfirmPassword: (String) -> Unit,
    updateFullname: (String) -> Unit,
    updateEmail: (String) -> Unit,
    updatePhone: (String) -> Unit,
    updateAddress: (String) -> Unit,
    updateRefererUsername: (String) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var refererUsername by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val keyboardOptions = KeyboardOptions()
    val keyboardActions = KeyboardActions()

    val contentColor = MaterialTheme.colorScheme.onSurface

    val headerTextStyle = MaterialTheme.typography.titleLarge.copy(
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
    )

    val captionStyle = MaterialTheme.typography.labelLarge.copy(contentColor)
    val bottomTextStyle = MaterialTheme.typography.titleSmall.copy(contentColor)
    val bottomActionTextStyle = MaterialTheme.typography.titleSmall.copy(
        MaterialTheme.colorScheme.onSurface
    )

    var isViewActive by remember { mutableStateOf(false) }

    LaunchedEffect(
        authenticationUiState.isLoading,
        authenticationUiState.error
    ) {
        isViewActive = authenticationUiState.isLoading != true &&
                authenticationUiState.error == null
    }

    LaunchedEffect(username) {
        updateUsername(username)
    }
    LaunchedEffect(password) {
        updatePassword(password)
    }
    LaunchedEffect(confirmPassword) {
        updateConfirmPassword(confirmPassword)
    }
    LaunchedEffect(phone) {
        updatePhone(phone)
    }
    LaunchedEffect(email) {
        updateEmail(email)
    }
    LaunchedEffect(fullname) {
        updateFullname(fullname)
    }
    LaunchedEffect(address) {
        updateAddress(address)
    }
    LaunchedEffect(refererUsername) {
        updateRefererUsername(refererUsername)
    }

    val resetPendingValues: () -> Unit = {
        username = ""
        password = ""
        confirmPassword = ""
        fullname = ""
        email = ""
        phone = ""
        address = ""
        refererUsername = ""
        isPasswordVisible = false
        isConfirmPasswordVisible = false
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

    val confirmPasswordTrailingIcon: @Composable () -> Unit = {
        if (isConfirmPasswordVisible) {
            IconButton(
                onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
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
                onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
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

    LaunchedEffect(authState) {
        resetPendingValues.invoke()
    }

    PoshScaffold(
        toolbar = {
            PoshToolbar(
                title = {},
                navigation = {
                    IconButton(
                        enabled = isViewActive,
                        onClick = onBackPressed
                    ) {
                        Icon(
                            painterResource(PoshIcon.ArrowBack),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.TopCenter) {

            AnimatedContent(
                modifier = Modifier,
                targetState = currentAuthenticationPage,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(
                            Settings.ANIMATION_DURATION_MILLIS_FAST,
                            easing = FastOutSlowInEasing
                        )
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(
                                    Settings.ANIMATION_DURATION_MILLIS_FAST,
                                    easing = FastOutSlowInEasing
                                )
                            )
                },
                label = "AuthenticationAnimation"
            ) { page ->
                PoshColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painterResource(id = PoshIcon.Logo),
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                    )
                    Spacer(Modifier.height(12.dp))

                    val headerTextRes: Int? = when (authState) {
                        AuthState.LOGIN -> R.string.login
                        AuthState.SIGNUP -> R.string.create_an_account

                        else -> null
                    }


                    val placeholder: @Composable (text: String) -> Unit = { t ->
                        Text(
                            text = t,
                            style = MaterialTheme.typography.bodyLarge.copy(contentColor.copy(0.4f))
                        )
                    }

                    headerTextRes?.let { h ->
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


                    when (authState) {
                        AuthState.LOGIN -> {
                            Spacer(Modifier.height(75.dp))

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

                        }

                        AuthState.SIGNUP -> {
                            when (page) {
                                1 -> {
                                    Text(
                                        text = stringResource(id = R.string.fullname),
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
                                        value = fullname,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.fullname)) },
                                        onValueChange = { fullname = it },
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
                                        text = stringResource(id = R.string.email),
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
                                        value = email,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.email)) },
                                        onValueChange = { email = it },
                                        keyboardOptions = keyboardOptions.copy(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next
                                        ),
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
                                        text = stringResource(id = R.string.phone_number),
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
                                        value = phone,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.phone_number)) },
                                        onValueChange = { phone = it },
                                        keyboardOptions = keyboardOptions.copy(
                                            keyboardType = KeyboardType.Phone,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = keyboardActions,
                                        visualTransformation = VisualTransformation.None,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                            )
                                    )

                                }

                                2 -> {
                                    Text(
                                        text = stringResource(id = R.string.address),
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
                                        value = address,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.address)) },
                                        onValueChange = { address = it },
                                        keyboardOptions = keyboardOptions.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = keyboardActions,
                                        visualTransformation = VisualTransformation.None,
                                        isMultiline = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                            )
                                    )

                                    Spacer(Modifier.height(12.dp))


                                    Text(
                                        text = stringResource(id = R.string.referer_username_opt),
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
                                        value = refererUsername,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.referer_username)) },
                                        onValueChange = { refererUsername = it },
                                        keyboardOptions = keyboardOptions.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = keyboardActions,
                                        visualTransformation = VisualTransformation.None,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                            )
                                    )

                                }

                                3 -> {
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
                                            imeAction = ImeAction.Next
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

                                    Spacer(Modifier.height(12.dp))


                                    Text(
                                        text = stringResource(id = R.string.confirm_password),
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
                                        value = confirmPassword,
                                        enabled = isViewActive,
                                        placeholder = { placeholder.invoke(stringResource(id = R.string.confirm_password)) },
                                        onValueChange = { confirmPassword = it },
                                        keyboardOptions = keyboardOptions.copy(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = keyboardActions,
                                        trailingIcon = confirmPasswordTrailingIcon,
                                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else
                                            PasswordVisualTransformation('*'),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                            )
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    PoshCheckboxWithText(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp,
                                            ),
                                        isChecked = authenticationRequest.hasAgreedToTermsAndPrivacyPolicy == true,
                                        onCheckedChange = onCheckedTermsAndPrivacyPolicy,
                                        enabled = isViewActive,
                                        onClickText = onTermsAndPrivacyClicked,
                                    )

                                }
                            }

                            Spacer(Modifier.height(75.dp))

                        }

                        else -> Unit
                    }
                }
            }

            PoshColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if(authState == AuthState.SIGNUP) {
                    PoshCircularTabGroup(
                        modifier = Modifier,
                        selectedIndex = currentAuthenticationPage - 1,
                    )

                    Spacer(Modifier.height(22.dp))
                }

                val buttonTextRes: Int? = when (authState) {
                    AuthState.LOGIN -> R.string.login
                    AuthState.SIGNUP -> {
                        if (currentAuthenticationPage < AuthenticationViewModel.TOTAL_AUTHENTICATION_PAGES) {
                            R.string.next
                        } else {
                            R.string.create_account
                        }
                    }

                    AuthState.PASSWORD_RESET -> R.string.submit
                    else -> null
                }

                buttonTextRes?.let { res ->
                    PoshButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        text = stringResource(id = res),
                        enabled = isViewActive && authenticationRequest.isValid(currentAuthenticationPage),
                        onClick = onAction,
                        role = ButtonRole.PRIMARY,
                    )
                    Spacer(Modifier.height(12.dp))
                }

                var alternateTextRes: Int? = null
                var alternateActionTextRes: Int? = null

                when (authState) {
                    AuthState.LOGIN -> {
                        alternateTextRes = R.string.are_you_a_new_user
                        alternateActionTextRes = R.string.create_account
                    }

                    AuthState.SIGNUP -> {
                        alternateTextRes = R.string.already_have_an_account
                        alternateActionTextRes = R.string.login
                    }

                    else -> Unit
                }

                if (alternateTextRes != null && alternateActionTextRes != null) {
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
                }

                Spacer(Modifier.height(72.dp))
            }
        }

        if(authenticationUiState.isLoading == true) {
            PoshLoader()
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