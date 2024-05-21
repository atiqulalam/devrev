package com.example.devrev.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Gray,
    progressColor: Color = Color.Green,
    strokeWidth: Float = 8f,
    canvasColor: Color = Color.Black,
    textColor: Color = Color.White,
    textSize: Float = 20f
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = modifier) {
            // Fill the canvas with the specified color
            drawCircle(color = canvasColor)
            // Draw the background circle
            drawCircle(
                color = backgroundColor,
                style = Stroke(width = strokeWidth)
            )

            // Draw the progress arc
            val sweepAngle = progress/100
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${(progress / 100).toInt()}%",
            color = textColor,
            fontSize = textSize.sp
        )
    }
}

@Preview
@Composable
fun PreviewCircularProgressBar() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressBar(
                    progress = 0.75f,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}
