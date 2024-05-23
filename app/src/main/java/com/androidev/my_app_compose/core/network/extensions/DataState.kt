package com.androidev.my_app_compose.core.network.extensions

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
    data class OtherError(val msg: String) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
}