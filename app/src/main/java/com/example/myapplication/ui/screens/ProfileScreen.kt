package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
// Text: "Fraunces", Georgia, serif;
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F2)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}