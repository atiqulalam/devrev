package com.devrevsdk

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

open class NetworkClient private constructor(
    private val baseUrl: String,
    private val defaultHeaders: Map<String, String> = mapOf(),
    private val isLoggingEnabled: Boolean,
    private val converterFactory: ConverterFactory?
) {
    class Builder {
        private var baseUrl: String = ""
        private var connectTimeout = 10000 // 10 seconds
        private var readTimeout = 10000 // 10 seconds
        private val headers: MutableMap<String, String> = mutableMapOf()
        private var isLoggingEnabled: Boolean = false
        private var converterFactory: ConverterFactory? = null

        fun setConnectionTimeout(connectTimeout: Int) = apply {
            this.connectTimeout = connectTimeout
        }
        fun setReadTimeout(readTimeout: Int) = apply {
            this.readTimeout = readTimeout
        }

        fun setBaseUrl(url: String) = apply {
            this.baseUrl = url
        }

        fun addHeader(key: String, value: String) = apply {
            headers[key] = value
        }

        fun enableLogging(enable: Boolean) = apply {
            this.isLoggingEnabled = enable
        }

        fun setConverterFactory(converterFactory: ConverterFactory) = apply {
            this.converterFactory = converterFactory
        }

        fun build(): NetworkClient {
            return NetworkClient(baseUrl, headers, isLoggingEnabled, converterFactory)
        }
    }

    suspend fun get(endpoint: String,
                    queryParams: Map<String, String> = emptyMap(),
                    headers: Map<String, String> = emptyMap(),
                    callback: NetworkCallback? = null) {
        val url = URL(baseUrl + appendQueryParams(endpoint, queryParams))
        try {
            withContext(Dispatchers.IO) {
                val connection = url.openConnection() as HttpURLConnection
                val requestHeaders = headers + defaultHeaders
                connection.apply {
                    requestMethod = "GET"
                    connectTimeout = connectTimeout
                    readTimeout = readTimeout
                    requestHeaders.forEach { (key, value) -> setRequestProperty(key, value) }
                }

                if (isLoggingEnabled) {
                    logRequest(url.toString(), requestHeaders, "")
                }

                // Get response code
                val statusCode = connection.responseCode
                // Get response headers
                val responseHeaders = connection.headerFields
                    .filterKeys { it != null }
                    .mapValues { it.value.joinToString(", ") }

                // Read the response
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                // Disconnect the connection
                connection.disconnect()
                if (isLoggingEnabled) {
                    logResponse(statusCode, responseHeaders, response.toString())
                }
                // Return the response
                val apiResponse = NetworkResponse(statusCode, responseHeaders,  response.toString())
                withContext(Dispatchers.Main) {
                    if (statusCode in 200..299){
                        callback?.onSuccess(apiResponse)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                                Build.VERSION_CODES.S) >= 7) {
                            callback?.onFailure(HttpException("SomeThing Went Wrong... ", Throwable("HTTP error code: $statusCode")))
                        } else {
                            callback?.onFailure(Exception("SomeThing Went Wrong... ", Throwable("HTTP error code: $statusCode")))
                        }
                    }
                }
            }
        }catch (e: Exception) {
            withContext(Dispatchers.Main) {
                callback?.onFailure(e)
            }
        }
    }


    suspend fun post(endpoint: String, postData: Any, queryParams: Map<String, String> = emptyMap(), headers: Map<String, String> = emptyMap(), callback: NetworkCallback) {
        val requestBody = converterFactory?.toJson(postData) ?: throw IllegalArgumentException("ConverterFactory is not set")
        val url = URL(baseUrl + appendQueryParams(endpoint, queryParams))

        try {
            withContext(Dispatchers.IO){
                val connection = url.openConnection() as HttpURLConnection
                // Set the request method
                val requestHeaders = headers + defaultHeaders
                connection.apply {
                    doOutput = true
                    requestMethod = "POST"
                    connectTimeout = connectTimeout
                    readTimeout = readTimeout
                    requestHeaders.forEach { (key, value) -> setRequestProperty(key, value) }
                }
                // Get response code
                val statusCode = connection.responseCode
                // Get response headers
                val responseHeaders = connection.headerFields
                    .filterKeys { it != null }
                    .mapValues { it.value.joinToString(", ") }

                // Write the post data to the connection
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(requestBody.toByteArray())
                outputStream.flush()
                outputStream.close()

                if (isLoggingEnabled) {
                    logRequest(requestBody, headers, requestBody)
                }

                // Read the response
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                // Disconnect the connection
                connection.disconnect()
                if (isLoggingEnabled) {
                    logResponse(statusCode, responseHeaders, response.toString())
                }

                // Return the response as a string
                val apiResponse = NetworkResponse(statusCode, responseHeaders,  response.toString())
                withContext(Dispatchers.Main) {
                    if (statusCode in 200..299){
                        callback.onSuccess(apiResponse)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                                Build.VERSION_CODES.S) >= 7) {
                            throw HttpException("SomeThing Went Wrong... ", Throwable("HTTP error code: $statusCode"))
                        } else {
                            throw Exception("SomeThing Went Wrong... ", Throwable("HTTP error code: $statusCode"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                callback.onFailure(e)
            }
        }
    }


    private fun appendQueryParams(endpoint: String, queryParams: Map<String, String>): String {
        if (queryParams.isEmpty()) return endpoint

        val queryString = queryParams.entries.joinToString("&") { (key, value) -> "$key=$value" }
        return if (endpoint.contains("?")) {
            "$endpoint&$queryString"
        } else {
            "$endpoint?$queryString"
        }
    }



    fun <T> parseResponse(responseBody: String, clazz: Class<T>): T? {
        return converterFactory?.fromJson(responseBody, clazz)
    }

    private fun logRequest(requestLine: String, headers: Map<String, String>, body: String) {
        println("HTTP Request:")
        println(requestLine)
        headers.forEach { (key, value) -> println("$key: $value") }
        if (body.isNotEmpty()) {
            println("\n$body")
        }
    }

    private fun logResponse(statusCode: Int, headers: Map<String, String>, body: String) {
        println("HTTP Response:")
        println("Status Code: $statusCode")
        headers.forEach { (key, value) -> println("$key: $value") }
        if (body.isNotEmpty()) {
            println("\n$body")
        }
    }
}



