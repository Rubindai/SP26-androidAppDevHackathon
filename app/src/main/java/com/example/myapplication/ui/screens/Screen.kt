package com.example.myapplication.ui.screens

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object OnboardingScreen : Screen()
    @Serializable
    data object DashboardScreen : Screen()

    @Serializable
    data object SearchScreen : Screen()

    @Serializable
    data class ProfileScreen(val userId: String) : Screen()

    // ...
    fun NavBackStackEntry.toScreen(): Screen? =
        when (destination.route?.substringAfterLast(".")?.substringBefore("/")) {
            "HomeScreen" -> toRoute<DashboardScreen>()
            "SettingsScreen" -> toRoute<SearchScreen>()
            "ProfileScreen" -> toRoute<ProfileScreen>()
            else -> null
        }
}