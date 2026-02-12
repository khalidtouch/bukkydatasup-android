package com.kxtdev.bukkydatasup.common.datastore

import android.content.Context
import com.kxtdev.bukkydatasup.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SharedPrefProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val pref = context.getSharedPreferences("${BASE_NAME}_pref", Context.MODE_PRIVATE)

    fun saveTransactionPin(pin: String) {
        with(pref.edit()) {
            putString(context.getString(R.string.key_transaction_pin), pin)
            apply()
        }
    }

    fun getTransactionPin(): String? {
        return pref.getString(context.getString(R.string.key_transaction_pin), null)
    }
}