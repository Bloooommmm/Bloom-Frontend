package id.ac.binus.bloomapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.ac.binus.bloomapp.screens.onboarding.OnboardingScreen
import id.ac.binus.bloomapp.screens.auth.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        // Onboarding Screen
        composable("onboarding") {
            OnboardingScreen(
                onGetStartedClick = {
                    navController.navigate("login")
                }
            )
        }

        // Login Screen
        composable("login") {
            LoginScreen(
                onGoogleSignInClick = {
                    // Google Sign In logic will be implemented later
                }
            )
        }
    }
}