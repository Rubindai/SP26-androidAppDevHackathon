package com.example.myapplication.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.screens.Screen

data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)