package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.NavItem
import com.example.myapplication.ui.screens.CompletedCoursesScreen
import com.example.myapplication.ui.screens.MajorSelectionScreen
import com.example.myapplication.ui.screens.OnboardingScreen
import com.example.myapplication.ui.screens.ProgressScreen
import com.example.myapplication.ui.screens.Screen
import com.example.myapplication.ui.screens.ScheduleScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.theme.AppCream
import com.example.myapplication.ui.theme.AppSurfaceSelected
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.OnboardingRequirementViewModel
import com.example.myapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val tabs = listOf(
                NavItem(
                    label = "Dashboard",
                    icon = Icons.Filled.Home,
                    screen = Screen.DashboardScreen,
                ),
                NavItem(
                    label = "Search",
                    icon = Icons.Default.Search,
                    screen = Screen.SearchScreen,
                ),
                NavItem(
                    label = "Schedule",
                    icon = Icons.Filled.CalendarMonth,
                    screen = Screen.ScheduleScreen,
                ),
            )

            MyApplicationTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                val onboardingViewModel: UserViewModel = hiltViewModel()
                val requirementsViewModel: OnboardingRequirementViewModel = hiltViewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != Screen.OnboardingScreen::class.qualifiedName &&
                            currentRoute != Screen.MajorSelectionScreen::class.qualifiedName &&
                            currentRoute != Screen.CompletedCoursesScreen::class.qualifiedName) {
                            NavigationBar(
                                containerColor = AppCream
                            ) {
                                tabs.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute.isSelectedRoute(item.screen),
                                        onClick = {
                                            navController.navigate(item.screen) {
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text(text = item.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = AppSurfaceSelected
                                        )
                                    )
                                }
                            }
                        }
                    },
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.OnboardingScreen
                        ) {
                            composable<Screen.OnboardingScreen> {
                                OnboardingScreen(
                                    viewModel = onboardingViewModel,
                                    onNext = {
                                        navController.navigate(Screen.MajorSelectionScreen) {
                                            popUpTo(Screen.OnboardingScreen) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable<Screen.MajorSelectionScreen> {
                                MajorSelectionScreen(
                                    viewModel = onboardingViewModel,
                                    onNext = {
                                        navController.navigate(Screen.CompletedCoursesScreen) {
                                            popUpTo(Screen.MajorSelectionScreen) { inclusive = true }
                                        }
                                    },
                                )
                            }
                            composable<Screen.CompletedCoursesScreen> {
                                CompletedCoursesScreen(
                                    viewModel = requirementsViewModel,
                                    onDone = {
                                        navController.navigate(Screen.DashboardScreen) {
                                            popUpTo(Screen.CompletedCoursesScreen) { inclusive = true }
                                        }
                                    },
                                )
                            }
                            composable<Screen.DashboardScreen> {
                                ProgressScreen()
                            }
                            composable<Screen.SearchScreen> {
                                SearchScreen()
                            }
                            composable<Screen.ScheduleScreen> {
                                ScheduleScreen()
                            }
                        }
                    }
                }
            }
        }

    }
}

private fun String?.isSelectedRoute(screen: Screen): Boolean =
    this?.substringBefore("/") == screen::class.qualifiedName
