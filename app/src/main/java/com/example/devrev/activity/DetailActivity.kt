package com.example.devrev.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.devrev.viewmodels.DetailsViewModel
import com.example.devrev.R
import com.example.devrev.models.Movie
import com.example.devrev.ui.compose.DetailScreen
import com.example.devrev.ui.theme.DevrevTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private var movie:Movie? = null
    private val detailsViewModel: DetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movie",Movie::class.java)
        } else {
            intent.getParcelableExtra("movie")
        }
        fetchDetails()
        setContent {
            DevrevTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    movie?.let { MainScreen(it,detailsViewModel) }
                }
            }
        }
    }

    private  fun fetchDetails() {
        detailsViewModel.getMovieDetails(movie?.id.toString())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(movie:Movie,viewModel: DetailsViewModel) {
    val context = LocalContext.current as ComponentActivity
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Details")
                },
                navigationIcon = {
                    IconButton(onClick = { context.finish() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Box {
                val isLoading by viewModel.detailsDataLoading.collectAsState(initial = false)
                if (isLoading){
                    androidx.compose.material.CircularProgressIndicator(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(45.dp)
                            .align(Alignment.Center)
                    )
                }
                DetailScreen(item = movie,viewModel)
            }

        }
    )
}