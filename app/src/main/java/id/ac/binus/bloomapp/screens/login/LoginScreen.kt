package id.ac.binus.bloomapp.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import id.ac.binus.bloomapp.R
import id.ac.binus.bloomapp.ui.theme.*

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .padding(horizontal = 28.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // Bloom Logo
        Image(
            painter = painterResource(R.drawable.bloom_text_logo),
            contentDescription = "Bloom Logo",
            modifier = Modifier
                .padding(top = 16.dp)
                .height(32.dp)
        )

        // Login Illustration
        Image(
            painter = painterResource(R.drawable.login_illustration),
            contentDescription = "Login Illustration",
            modifier = Modifier.size(264.dp),
            contentScale = ContentScale.Fit
        )

        // Headline & Subheadline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Your new journey starts here.",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sign in to track your cycle, explore lessons, and learn more about yourself.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }

        // Bottom Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Google Sign In Button
            // Google Sign In Button
            OutlinedButton(
                onClick = onGoogleSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = Color(0xFFD8D7D7)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF53575B)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Continue with Google",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF53575B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Disclaimer
            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}