package com.sisco.musicapp.utils

sealed class UiState<out R> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Failed<out T>(val errorCode: String, val failedMessage: String, val failedData: T? = null) : UiState<T>()
    data class Error(val throwable: Throwable, val errorMessage: String? = null) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}

inline fun <reified T> UiState<T>.doIfSuccess(callback: (data: T) -> Unit) {
    if (this is UiState.Success) callback(data)
}

inline fun <reified T> UiState<T>.doIfFailed(callback: (errorCode: String, errorMessage: String, failedData: T?) -> Unit) {
    if (this is UiState.Failed) callback(errorCode, failedMessage, failedData)
}

inline fun <reified T> UiState<T>.doIfError(callback: (throwable: Throwable, message: String?) -> Unit) {
    if (this is UiState.Error) callback(throwable, errorMessage)
}

inline fun <reified T> UiState<T>.doIfLoading(callback: () -> Unit) {
    if (this is UiState.Loading) callback()
}