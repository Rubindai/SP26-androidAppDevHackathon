package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressCircle(progress: Float) {
    Box(contentAlignment = Alignment.Center) {
        // background circle
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.size(60.dp),
            color = Color(0xFFE5E0D4),
            strokeWidth = 6.dp,
            strokeCap = StrokeCap.Round
        )
        // circle
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(60.dp),
            color = Color(0xFF8B2323),
            strokeWidth = 6.dp,
            strokeCap = StrokeCap.Round
        )
        // inner text
        Text(
            text = "${(progress * 100).toInt()}%",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun ProgressCirclePreview() {
    ProgressCircle(0.10f)
}