package com.rkt.myimbdmodernmovieapp

sealed class ResponseHandler<T>(val data: T? = null, val error: ErrorHandler? = null) {
    class Success<T>(data: T? = null) : ResponseHandler<T>(data = data)
    class Error<T>(err: ErrorHandler) : ResponseHandler<T>(error = err)
    class Loading<T>() : ResponseHandler<T>()
    class Empty<T>() : ResponseHandler<T>()
}
