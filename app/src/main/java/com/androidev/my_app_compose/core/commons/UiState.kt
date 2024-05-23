package com.androidev.my_app_compose.core.commons

interface UiState {
    data object Loading : UiState
    class Success<Q>(val response: Q) : UiState
    class Error(val msg: String) : UiState
}