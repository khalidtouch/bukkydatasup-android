package com.kxtdev.bukkydatasup.network.utils

import java.io.IOException


class InternetException: Exception() {
    override val cause: Throwable
        get() = Throwable("Failed to establish an internet connection")
    override val message: String
        get() = "You're currently offline"
    override fun printStackTrace() {
        print("You're are not connected to the internet.")
    }
}

class BadRequestException(private val error: String): Exception() {
    override val cause: Throwable
        get() = Throwable("Check your network request and try again")
    override fun printStackTrace() {
        print("There's something wrong in your request. Please check again")
    }
    override val message: String
        get() = error
}

class ServerErrorException: IOException() {
    override val cause: Throwable
        get() = Throwable("Server error. Please try again")
    override val message: String
        get() = "Internal Server Error"
    override fun printStackTrace() {
        print("Something came up on the server while processing your request.")
    }
}