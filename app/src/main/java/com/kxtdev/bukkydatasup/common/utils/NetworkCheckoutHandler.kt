package com.kxtdev.bukkydatasup.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kxtdev.bukkydatasup.BuildConfig

object NetworkCheckoutHandler {
    fun cardPaymentCheckout(context: Context, checkoutUrl: String?) {
        checkoutUrl?.let { url ->
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                context.startActivity(this)
            }
        }
    }
    fun openPrivacyPolicy(context: Context) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("${ BuildConfig.BASE_URL }/privacy/")
            context.startActivity(this)
        }
    }
}