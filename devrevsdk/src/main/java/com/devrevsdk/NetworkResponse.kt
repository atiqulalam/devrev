package com.devrevsdk

data class NetworkResponse(
    val statusCode: Int,
    val headers: Map<String, String>,
    val body: String
)
