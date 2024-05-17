package com.devrevsdk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.net.URL

class NetworkClient private constructor(
    private val baseUrl: String,
    private val defaultHeaders: Map<String, String>,
    private val isLoggingEnabled: Boolean,
    private val converterFactory: ConverterFactory?
) {

    class Builder {
        private var baseUrl: String = ""
        private val headers: MutableMap<String, String> = mutableMapOf()
        private var isLoggingEnabled: Boolean = false
        private var converterFactory: ConverterFactory? = null

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
    suspend fun get(endpoint: String,queryParams: Map<String, String> = emptyMap(), headers: Map<String, String> = emptyMap(), callback: NetworkCallback) {
        val request = NetworkRequest(
            url = appendQueryParams(endpoint, queryParams),
            method = "GET",
            headers = headers
        )
        execute(request, callback)
    }

    suspend fun post(endpoint: String, body: Any,queryParams: Map<String, String> = emptyMap(), headers: Map<String, String> = emptyMap(), callback: NetworkCallback) {
        val requestBody = converterFactory?.toJson(body) ?: throw IllegalArgumentException("ConverterFactory is not set")
        val request = NetworkRequest(
            url = appendQueryParams(endpoint, queryParams),
            method = "POST",
            headers = headers,
            body = requestBody
        )
        execute(request, callback)
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

    private suspend fun execute(request: NetworkRequest, callback: NetworkCallback) {
        try {
            withContext(Dispatchers.IO) {
                val url = URL(baseUrl + request.url)
                val port = if (url.port == -1) url.defaultPort else url.port
                val socket = Socket(url.host, port)
               // val output = OutputStreamWriter(socket.getOutputStream())
                val output = PrintWriter(socket.getOutputStream(),true)
                output.println("GET ${request.url} HTTP/1.1")
                output.println("Host: ${url.host}")
                output.println("Connection: close")
                output.println()
                output.flush()
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                // Construct the HTTP request
                val requestLine = "${request.method} ${url.file} HTTP/1.1\r\n"
                val requestHeaders = defaultHeaders + request.headers
                val requestBody = request.body ?: ""

                if (isLoggingEnabled) {
                    logRequest(requestLine, requestHeaders, requestBody)
                }

                // Read the HTTP response
                val responseHeaders = mutableMapOf<String, String>()
                var responseBody = ""
                var statusCode = 0
                while (input.readLine().also { if (it !=null) responseBody = it } != null){
                    if (statusCode == 0){
                        statusCode = responseBody.split(" ")[1].toInt()
                    }
                    if (responseBody.contains(":")) {
                        val headerParts = responseBody.split(":", limit = 2)
                        if (headerParts.size == 2 && !responseBody.startsWith("{")) {
                            responseHeaders[headerParts[0].trim()] = headerParts[1].trim()
                        }
                    }
                }


                if (isLoggingEnabled) {
                    logResponse(statusCode, responseHeaders, responseBody)
                }
                val response = NetworkResponse(statusCode, responseHeaders, responseBody)
                output.close()
                socket.close()

                withContext(Dispatchers.Main) {
                    callback.onSuccess(response)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                callback.onFailure(e)
            }
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



