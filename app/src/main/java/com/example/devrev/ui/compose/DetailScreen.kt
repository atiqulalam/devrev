package com.example.devrev.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devrev.viewmodels.DetailsViewModel
import com.example.devrev.R
import com.example.devrev.models.Movie
import com.example.devrev.models.MovieDetail
import com.example.devrev.utils.Util
import java.lang.StringBuilder

@Composable
fun DetailScreen(item: Movie,viewModel: DetailsViewModel) {
    val data by viewModel.detailsDataFlow.collectAsState(initial = MovieDetail())
    val imageUrl = StringBuilder("https://image.tmdb.org/t/p/w500")
        .append((item.poster_path?:item.backdrop_path))
        .toString()
    val imgBanner = StringBuilder("https://image.tmdb.org/t/p/w500")
        .append((item.backdrop_path))
        .toString()


    Column (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding( top = 60.dp)){
        Box (modifier = Modifier.fillMaxWidth().wrapContentHeight()){
            BlurredImage(modifier = Modifier.fillMaxWidth().wrapContentHeight(),imgBanner)
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(start = 8.dp, end = 8.dp,)
                //.padding(16.dp)
            ) {
                GlideImage(
                    imageUrl = imageUrl,
                    Modifier
                        .width(100.dp)
                        .height(160.dp),
                    errorPlaceholder = R.drawable.placeholder
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = data?.title.orEmpty(),
                        style = TextStyle(color = Color.White, fontStyle = FontStyle.Normal,fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.Start)
                    )
                    Text(
                        text = Util.commaSeparatedString(data?.genres?:ArrayList()),
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()){
                        CircularProgressBar(progress = (item.popularity?:0f).toFloat(), modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterVertically), textSize = 8f)
                        Text(
                            text = "User \nScore", style = TextStyle(fontSize = 12.sp,color = Color.White),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically), textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = data?.tagline.orEmpty(), style = TextStyle(fontSize = 12.sp,color = Color.White, fontStyle = FontStyle.Italic),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(start = 4.dp)
                            , textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Overview", style = TextStyle(fontSize = 12.sp,color = Color.White, fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(start = 4.dp)
                        , textAlign = TextAlign.Center
                    )
                    Text(
                        text = data?.overview.orEmpty(), style = TextStyle(fontSize = 12.sp,color = Color.White),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(start = 4.dp),
                        textAlign = TextAlign.Justify, maxLines = 4,overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }


}