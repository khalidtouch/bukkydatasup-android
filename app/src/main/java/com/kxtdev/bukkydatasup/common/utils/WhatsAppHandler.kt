package com.kxtdev.bukkydatasup.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kxtdev.bukkydatasup.BuildConfig
import java.net.URLEncoder

class WhatsAppHandler {
    fun joinGroupChat(context: Context, groupLink: String?) {
        groupLink?.let { link ->
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(link)
                context.startActivity(this)
            }
        }
    }

    fun chatSupport(context: Context, phone: String?, message: String) {
        phone?.let { p ->
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    BuildConfig.WHATSAPP_BASE_URL + "/${formatPhone(p)}?text=${
                        URLEncoder.encode(message, "UTF-8")
                    }"
                )
                context.startActivity(this)
            }
        }
    }

    private fun formatPhone(phone: String): String {
        var mPhone = phone
        if(mPhone.startsWith("0")) {
            mPhone = mPhone
                .removePrefix("0")
            mPhone = "+234$mPhone"
        } else if(mPhone.startsWith("234")) {
            mPhone = "+$mPhone"
        }
        return mPhone
    }
}