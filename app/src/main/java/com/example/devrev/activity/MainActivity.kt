package com.example.devrev.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import com.example.devrev.ui.theme.DevrevTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.devrev.viewmodels.MainViewModel
import com.example.devrev.ui.compose.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callApi()

        setContent {
            DevrevTheme {
                MainScreen(viewModel)
            }
        }
    }

    private fun callApi() {
        viewModel.fetchPopular()
        viewModel.fetchTopRated()
    }

}




