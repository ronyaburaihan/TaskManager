package com.techdoctorbd.taskmanagermongodb.utils

import retrofit2.Response

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}

fun <T> handleNetworkResponse(response: Response<T>): NetworkResult<T> {
    return when {
        response.message().toString().contains("timeout") -> {
            NetworkResult.Error("Request timeout")
        }

        response.isSuccessful -> {
            val result = response.body()
            NetworkResult.Success(result!!)
        }

        else -> {
            NetworkResult.Error(response.message())
        }
    }
}