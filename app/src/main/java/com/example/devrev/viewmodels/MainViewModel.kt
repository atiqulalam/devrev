package com.example.devrev.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrevsdk.NetworkCallback
import com.devrevsdk.NetworkClient
import com.devrevsdk.NetworkResponse
import com.example.devrev.BaseApplication
import com.example.devrev.BuildConfig
import com.example.devrev.models.ApiResponse
import com.example.devrev.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkClient: NetworkClient
) : ViewModel() {
    private val _popularDataFlow = MutableStateFlow<ArrayList<Movie>>(ArrayList())
    val popularDataFlow: StateFlow<ArrayList<Movie>> = _popularDataFlow.asStateFlow()

    private val _popularDataLoading = MutableStateFlow(false)
    val popularDataLoading: StateFlow<Boolean> = _popularDataLoading.asStateFlow()


    private val _topRatedDataFlow = MutableStateFlow<ArrayList<Movie>>(ArrayList())
    val topRatedDataFlow: StateFlow<ArrayList<Movie>> = _topRatedDataFlow.asStateFlow()

    private val _topRatedDataLoading = MutableStateFlow(false)
    val topRatedDataLoading: StateFlow<Boolean> = _topRatedDataLoading.asStateFlow()

    var popularPage = 1;
    var latestPage = 1;


    fun fetchPopular(pageNumber:Int = popularPage) {
        _popularDataLoading.value = true
        viewModelScope.launch {

            networkClient.get("/3/movie/popular",
                queryParams = mapOf("language" to "en-US","page" to pageNumber.toString(),
                    "region" to "US|IN|UK","api_key" to BuildConfig.API_KEY
                ),
                callback = object : NetworkCallback {
                override fun onSuccess(response: NetworkResponse) {
                    val post = networkClient.parseResponse(response.body, ApiResponse::class.java)
                    _popularDataFlow.value = ((_popularDataFlow.value + post?.results!!) as ArrayList<Movie>)
                    popularPage ++
                    _popularDataLoading.value = false
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(BaseApplication.application,error.message,Toast.LENGTH_SHORT).show()
                    _popularDataLoading.value = false
                }
            })
        }
    }

    fun fetchTopRated(pageNumber:Int = latestPage) {
        _topRatedDataLoading.value = true
        viewModelScope.launch {

            networkClient.get("/3/movie/top_rated",
                queryParams = mapOf("language" to "en-US","page" to pageNumber.toString(),
                    "region" to "US|IN|UK","api_key" to BuildConfig.API_KEY
                ),
                callback = object : NetworkCallback {
                    override fun onSuccess(response: NetworkResponse) {
                        val post = networkClient.parseResponse(response.body, ApiResponse::class.java)
                        _topRatedDataFlow.value = ((_topRatedDataFlow.value + post?.results!!) as ArrayList<Movie>)
                        latestPage ++
                        _topRatedDataLoading.value = false
                    }

                    override fun onFailure(error: Throwable) {
                        Toast.makeText(BaseApplication.application,error.message,Toast.LENGTH_SHORT).show()
                        _topRatedDataLoading.value = false
                    }
                })
        }
    }

}
