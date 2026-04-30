package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.NavItem
import com.example.myapplication.data.model.Student
import com.example.myapplication.ui.screens.OnboardingScreen

import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.ProgressScreen
import com.example.myapplication.ui.screens.RequirementsChecklistScreen
import com.example.myapplication.ui.screens.Screen
import com.example.myapplication.ui.screens.Screen.DashboardScreen.toScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.OnboardingRequirementViewModel
import com.example.myapplication.viewmodel.SearchViewModel
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
                    label = "Profile",
                    icon = Icons.Filled.Person,
                    screen = Screen.ProfileScreen(userId = "123"),
                )
            )

            MyApplicationTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                val searchViewModel: SearchViewModel = hiltViewModel()
                val requirementsViewModel: OnboardingRequirementViewModel = hiltViewModel()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != Screen.OnboardingScreen::class.qualifiedName &&
                            currentRoute != Screen.OnboardingRequirementScreen::class.qualifiedName) { // this
                            // makes it so the bottom bar will not show up in the onboarding screen
                            NavigationBar(
                                containerColor = Color(0xFFF9F7F2)
                            ) {
                                tabs.map { item ->
                                    NavigationBarItem(
                                        // before, it didn't recognize Dashboard and ProfileScreen as a screen
                                        // because they are objects, so this new change makes it work now
                                        // After you implement the profile screen as an object, it should
                                        // start displaying the pill background as well
                                        selected = currentRoute == item.screen::class.qualifiedName,
                                        onClick = { navController.navigate(item.screen) },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text(text = item.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = Color(0xFFF2E7E7)
                                        )
                                    )
                                }
                            }
                        }
                }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.OnboardingScreen
                        ) {
                            composable<Screen.OnboardingScreen> {
                                OnboardingScreen(
                                    onClick = {
                                        navController.navigate(Screen.OnboardingRequirementScreen) {
                                            // clears the Onboarding screen from the backstack
                                            // so the user can't "Go Back" to the login page
                                            popUpTo(Screen.OnboardingScreen) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable<Screen.OnboardingRequirementScreen> {
                                RequirementsChecklistScreen(
                                    student = Student(completed = requirementsViewModel.completedCourseIds),
                                    onToggleCourse = { courseId ->
                                        requirementsViewModel.toggleCourse(courseId)
                                    },
                                    onDone = {
                                        requirementsViewModel.finishOnboarding()
                                        navController.navigate(Screen.DashboardScreen) {
                                            // Now we clear the checklist from the stack too
                                            popUpTo(Screen.OnboardingRequirementScreen) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable<Screen.DashboardScreen> {
                                ProgressScreen()
                            }
                            composable<Screen.SearchScreen> {
                                SearchScreen(viewModel = searchViewModel)
                            }
                            composable<Screen.ProfileScreen> {
                                ProfileScreen()
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}