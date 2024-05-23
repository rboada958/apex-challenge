package com.androidev.my_app_compose.core.commons

sealed interface StateImpl {
    data object Loading : StateImpl
    class Success<Q>(val response: Q) : StateImpl
    class Error(val msg: String) : StateImpl
}