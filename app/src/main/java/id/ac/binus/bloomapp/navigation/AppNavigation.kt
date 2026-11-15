
package id.ac.binus.bloomapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.ac.binus.bloomapp.screens.auth.LoginScreen
import id.ac.binus.bloomapp.screens.onboarding.OnboardingScreen
import id.ac.binus.bloomapp.screens.today.TodayScreen
import id.ac.binus.bloomapp.ui.components.BloomBottomNavigationBar
import id.ac.binus.bloomapp.ui.screens.calendar.CalendarScreen
import id.ac.binus.bloomapp.ui.theme.Surface

private val mainRoutes = listOf(
    "today",
    "calendar",
    "lessons",
    "profile"
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = mainRoutes.indexOf(currentRoute)
    val showBottomBar = selectedIndex != -1

    Scaffold(
        containerColor = Surface,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                BloomBottomNavigationBar(
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        val route = mainRoutes[index]

                        navController.navigate(route) {
                            popUpTo("today") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "onboarding"
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    onGetStartedClick = {
                        navController.navigate("login")
                    }
                )
            }

            composable("login") {
                LoginScreen(
                    onGoogleSignInClick = {
                        navController.navigate("today") {
                            popUpTo("onboarding") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("today") {
                TodayScreen()
            }

            composable("calendar") {
                CalendarScreen()
            }

            composable("lessons") {
                PlaceholderScreen("Lessons")
            }

            composable("profile") {
                PlaceholderScreen("Profile")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}
