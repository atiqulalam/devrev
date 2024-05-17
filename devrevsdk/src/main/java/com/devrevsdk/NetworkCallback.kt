package com.devrevsdk

interface NetworkCallback {
    fun onSuccess(response: NetworkResponse)
    fun onFailure(error: Throwable)
}
