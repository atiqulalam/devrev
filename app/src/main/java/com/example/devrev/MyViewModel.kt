package com.example.devrev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrevsdk.NetworkCallback
import com.devrevsdk.NetworkClient
import com.devrevsdk.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val networkClient: NetworkClient
) : ViewModel() {

    fun fetchPost(pageNumber:Int =1) {
        viewModelScope.launch {
            networkClient.get("/3/movie/popular",
                queryParams = mapOf("language" to "en-US","page" to pageNumber.toString(),
                    "region" to "US|IN|UK","api_key" to "909594533c98883408adef5d56143539"),
                callback = object : NetworkCallback {
                override fun onSuccess(response: NetworkResponse) {
                    println("GET Response: ${response.body}")
                    val post = networkClient.parseResponse(response.body, Any::class.java)
                    println("Parsed GET Response: $post")
                }

                override fun onFailure(error: Throwable) {
                    println("GET Error: ${error.message}")
                }
            })
        }
    }
}
