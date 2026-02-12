package com.kxtdev.bukkydatasup.common.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import com.kxtdev.bukkydatasup.ui.design.PoshOutlinedTextField


fun LazyListScope.enterPhone(
    value: String,
    onValueChange: (String) -> Unit,
    selectedNetwork: Network?,
    onShowDialog: () -> Unit,
    updateNetwork: (Network) -> Unit,
    enabled: Boolean
    ) {
    item {
        val textColor = MaterialTheme.colorScheme.onTertiary
        val captionStyle = MaterialTheme.typography.labelLarge.copy(
            MaterialTheme.colorScheme.onPrimary
        )
        val phoneStyle = MaterialTheme.typography.titleLarge.copy(
            color = textColor, fontWeight = FontWeight.Bold)
        val context = LocalContext.current

        var keyboardOptions by remember { mutableStateOf(KeyboardOptions()) }

        LaunchedEffect(value) {
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            onValueChange.invoke(value.replace("+234", "0").replace(" ", ""))
            if(value.length >= 5) {
                Network.checkPhone(value)?.let { calculatedNetwork ->
                    updateNetwork(calculatedNetwork)
                }
            }
        }

        val contactIntent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }

        val onContactActivityResult: (ActivityResult) -> Unit = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri: Uri? = result.data?.data

                val projection: Array<String> = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                )

                contactUri?.let {
                    context.contentResolver.query(it, projection, null, null, null)
                        .use { cursor ->
                            cursor?.let { curs ->
                                if (curs.moveToFirst()) {
                                    val numberIndex =
                                        curs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    val number = curs.getString(numberIndex)
                                    onValueChange.invoke(number.replace("+234", "0").replace(" ", ""))
                                }
                            }
                        }
                }
            }
        }

        val resultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = onContactActivityResult
        )

        val contactPermissionCallback: (Boolean) -> Unit = {
            resultLauncher.launch(contactIntent)
        }

        val contactPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = contactPermissionCallback
        )

        val onPickContact: () -> Unit = {
            when(PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) -> resultLauncher.launch(contactIntent)

                else -> contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }

        val mSelectedNetwork = selectedNetwork ?: Network.MTN

        LaunchedEffect(selectedNetwork) {
            updateNetwork(mSelectedNetwork)
        }

        val placeholder: @Composable () -> Unit = {
            Text(
                text = stringResource(id = R.string.enter_phone),
                style = phoneStyle.copy(phoneStyle.color.copy(0.8f))
            )
        }

        val leadingIcon: @Composable () -> Unit = {
            Row(
                Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    enabled = enabled,
                    onClick = onShowDialog
                ) {
                    Image(
                        painterResource(id = mSelectedNetwork.icon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(38.dp)
                    )
                }
                Spacer(Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = phoneStyle.color
                )
            }
        }

        val trailingIcon: @Composable () -> Unit = {
            IconButton(
                enabled = enabled,
                onClick = onPickContact,
            ) {
                Icon(
                    painterResource(id = PoshIcon.PickContact),
                    contentDescription = null,
                    tint = textColor
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Text(
            text = stringResource(id = R.string.enter_phone_number),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = value,
            enabled = enabled,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(),
            visualTransformation = VisualTransformation.None,
            leadingIcon = leadingIcon,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            textStyle = phoneStyle
        )
        Spacer(Modifier.height(12.dp))
    }
}