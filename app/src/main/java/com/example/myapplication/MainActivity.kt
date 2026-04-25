package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.model.NavItem
import com.example.myapplication.screens.DashboardScreen
import com.example.myapplication.screens.ProfileScreen
import com.example.myapplication.screens.Screen
import com.example.myapplication.screens.Screen.DashboardScreen.toScreen
import com.example.myapplication.screens.SearchScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
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
                    icon = Icons.Filled.Add,
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

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        tabs.map { item ->
                            NavigationBarItem(
                                selected = item.screen == navBackStackEntry?.toScreen(),
                                onClick = { navController.navigate(item.screen) },
                                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                                label = { Text(text = item.label) }
                            )
                        }
                    }
                }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.DashboardScreen
                        ) {
                            composable<Screen.DashboardScreen> {
                                DashboardScreen()
                            }
                            composable<Screen.SearchScreen> {
                                SearchScreen()
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