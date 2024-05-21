package com.example.devrev.ui.compose

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devrev.viewmodels.MainViewModel
import com.example.devrev.activity.DetailActivity

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ToolBar with Header") }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                Header("What's Popular")
                PopularMovieRecyclerView(viewModel)
                Header("Top Rated")
                TopRatedMovieRecyclerView(viewModel)
            }
        }
    )
}
@Composable
fun Header(title:String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = title, fontSize = 24.sp, style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun PopularMovieRecyclerView(
    viewModel: MainViewModel
) {
    val data by viewModel.popularDataFlow.collectAsState(initial = ArrayList())
    val lazyListState = rememberLazyListState()
    val isLoading by viewModel.popularDataLoading.collectAsState(initial = false)
    val context = LocalContext.current

    if (lazyListState.isScrollInProgress) {
        val totalItems = lazyListState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

        // If the last visible item index is equal to or greater than the total items, trigger the callback
        if (lastVisibleItemIndex >= totalItems - 1 && !isLoading) {
            viewModel.fetchPopular()
        }
    }
    Box {
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(data) { item ->
                    MovieItemView(item, onItemClick = {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.apply { putExtra("movie",it) }
                        context.startActivity(intent)
                    })
                }
                item {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                                .align(Alignment.Center))
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun TopRatedMovieRecyclerView(
    viewModel: MainViewModel
) {
    val data by viewModel.topRatedDataFlow.collectAsState(initial = ArrayList())
    val isLoading by viewModel.topRatedDataLoading.collectAsState(initial = false)
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    if (lazyListState.isScrollInProgress) {
        val totalItems = lazyListState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

        // If the last visible item index is equal to or greater than the total items, trigger the callback
        if (lastVisibleItemIndex >= totalItems - 1 && !isLoading) {
            viewModel.fetchTopRated()
        }
    }
    Box {
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(data) { item ->
                    MovieItemView(item, onItemClick ={
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.apply { putExtra("movie",it) }
                        context.startActivity(intent)
                    })
                }
                item {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                                .align(Alignment.Center))
                        }
                    }
                }
            }
        )
    }
}