package com.kxtdev.bukkydatasup.network.utils


sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failed<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String) : NetworkResult<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun<T> failed(data: T) = Failed(data)
        fun <T> error(message: String) = Error<T>(message)
    }

    inline fun onSuccess(block: (T) -> Unit): NetworkResult<T> = apply {
        if (this is Success) {
            block(data)
        }
    }

    inline fun onError(block: (String) -> Unit): NetworkResult<T> = apply {
        if (this is Error) {
            block(message)
        }
    }

    inline fun onFailure(block: (T) -> Unit): NetworkResult<T> = apply {
        if(this is Failed) {
            block(data)
        }
    }
}