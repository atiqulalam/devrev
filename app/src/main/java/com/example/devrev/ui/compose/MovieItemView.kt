package com.example.devrev.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devrev.R
import com.example.devrev.models.Movie
import com.example.devrev.utils.Util
import java.lang.StringBuilder

@Composable
fun MovieItemView(item: Movie,onItemClick: (Movie) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable {
                onItemClick(item)
            }
    ) {

        Column(
            modifier = Modifier
                .width(100.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUrl = StringBuilder("https://image.tmdb.org/t/p/w500")
                .append((item.poster_path?:item.backdrop_path))
                .toString()
            Box {
                Card ( modifier = Modifier
                    .wrapContentSize(),
                    elevation = 4.dp){
                    GlideImage(imageUrl = imageUrl,
                        Modifier
                            .width(100.dp)
                            .height(140.dp),
                        errorPlaceholder = R.drawable.placeholder)
                }

                CircularProgressBar(progress = (item.popularity?:0f).toFloat(), modifier = Modifier.size(30.dp).padding(start = 10.dp, top = 10.dp)
                    .align(Alignment.BottomStart), textSize = 8f)
            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(text = item.title?:"", modifier = Modifier
                .wrapContentWidth()
                .height(30.dp),
                fontSize = 14.sp,
                maxLines = 2,
                style = TextStyle(fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = Util.convertDate(item.release_date?:""), textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Start),
                fontSize = 12.sp,
                maxLines = 1,
                style = TextStyle(fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
        }
    }
}