package com.example.devrev.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrevsdk.NetworkCallback
import com.devrevsdk.NetworkClient
import com.devrevsdk.NetworkResponse
import com.example.devrev.BuildConfig
import com.example.devrev.models.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor (
    val networkClient: NetworkClient
):ViewModel() {
    private val _detailsDataFlow = MutableStateFlow<MovieDetail?>(MovieDetail())
    val detailsDataFlow: StateFlow<MovieDetail?> = _detailsDataFlow.asStateFlow()

    private val _detailsDataLoading = MutableStateFlow(false)
    val detailsDataLoading: StateFlow<Boolean> = _detailsDataLoading.asStateFlow()
    fun getMovieDetails(movieId:String){
        _detailsDataLoading.value = true
        viewModelScope.launch {
            networkClient.get("/3/movie/$movieId",
                queryParams = mapOf("language" to "en-US","api_key" to BuildConfig.API_KEY),
                callback = object : NetworkCallback {
                    override fun onSuccess(response: NetworkResponse) {
                        val post = networkClient.parseResponse(response.body, MovieDetail::class.java)
                        _detailsDataFlow.value = post
                        _detailsDataLoading.value = false
                    }

                    override fun onFailure(error: Throwable) {
                        _detailsDataLoading.value = false
                    }
                })
        }
    }
}