package com.androidev.my_app_compose.core.network.extensions

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.FlowCollector
import org.json.JSONObject

suspend fun <Q> FlowCollector<DataState<Q>>.runApiCall(
    success: suspend ApiResponse.Success<Q>.() -> Unit,
    error: suspend ApiResponse.Failure.Error<Q>.() -> Unit = {
        try {
            val jObjError = errorBody?.string()?.let { JSONObject(it) }
            val errorObject = jObjError?.getString("error")
            val convert = Gson().fromJson(errorObject, ErrorData::class.java)
            val otherError = convert.message.first()
            emit(DataState.OtherError(otherError))
        } catch (e: Exception) {
            emit(DataState.OtherError("Something unexpected happened"))
        }
    },
    exception: suspend ApiResponse.Failure.Exception<Q>.() -> Unit = {
        this.exception.printStackTrace()
        if (this.exception.message!!.contains("Unable to resolve host"))
            emit(DataState.OtherError("We cannot process your request."))
        else emit(DataState.Error(this.exception))
    },
    apiCall: suspend () -> ApiResponse<Q>,
) =
    this.run {
        apiCall.invoke()
            .suspendOnSuccess(success)
            .suspendOnError(error)
            .suspendOnException(exception)
    }

data class ErrorData(
    @SerializedName("message") val message: List<String>
)