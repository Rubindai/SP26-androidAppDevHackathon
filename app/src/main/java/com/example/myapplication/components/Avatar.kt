package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Fraunces


@Composable
fun Avatar(
    initial: String,

) {
    Box(modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(Color(0xFFFFF6EC)),
        contentAlignment = Alignment.Center){
        Text(
            text = initial,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            fontFamily = Fraunces,
            fontSize = (36 * 0.38f).sp,
            fontWeight = FontWeight.Bold
        )

    }
}


@Preview
@Composable
fun PreviewAvatar(){
    Avatar("AC")
}