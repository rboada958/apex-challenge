package com.androidev.my_app_compose.core.commons

sealed interface State {
    data object Loading : State
    class Success<Q>(val response: Q) : State
    class Error(val msg: String) : State
}