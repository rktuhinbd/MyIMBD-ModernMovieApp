package com.rkt.myimbdmodernmovieapp

sealed class UIState<T>(
    var data: T? = null,
    errorType: ErrorHandler.ErrorType? = null,
    val error: String? = null
) {
    class Success<T>(data: T? = null) : UIState<T>(data = data)
    class Error<T>(
        error: String,
        errorType: ErrorHandler.ErrorType = ErrorHandler.ErrorType.UNKNOWN
    ) : UIState<T>(error = error, errorType = errorType)

    class Loading<T> : UIState<T>()
    class Empty<T> : UIState<T>()
}