package com.kxtdev.bukkydatasup.network.utils

import com.google.gson.JsonParser
import retrofit2.Response


inline fun <reified T> Response<T>.getResponse(): T = this.body() as T

@Suppress("DEPRECATION")
suspend fun <T> networkCall(
    networkHelper: NetworkConnectionHelper,
    service: suspend () -> Response<T>
): Response<T> {
    if(networkHelper.isConnected()){
        val attempt = service.invoke()
        val success = attempt.isSuccessful
        if(success) {
            return attempt
        } else {
            val code = attempt.code().toString()
            when {
                code.startsWith("4") -> {
                    val errorString = attempt.errorBody()!!.string()
                    var message: String? = null
                    val fields = listOf(
                        "error",
                        "non_field_errors",
                        "new_password1",
                        "new_password2",
                        "old_password",
                        "detail",
                        "message",
                    )

                    fields.forEach { f ->
                        try {
                            val errorField = JsonParser().parse(errorString).asJsonObject[f].asString
                            message = errorField
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    val msg: String = when {
                        !message.isNullOrBlank() -> message!!
                        else -> "Something went wrong while processing your request"
                    }

                    throw BadRequestException(msg)
                }
                else -> throw ServerErrorException()
            }
        }
    } else {
        throw InternetException()
    }
}