
package id.ac.binus.bloomapp.screens.onboarding

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
import id.ac.binus.bloomapp.R
import id.ac.binus.bloomapp.ui.theme.*
import id.ac.binus.bloomapp.ui.components.CustomButton

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        // Top Background Illustration
        Image(
            painter = painterResource(R.drawable.bg_onboarding_illustration),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bloom Logo
            Spacer(modifier = Modifier.height(400.dp))

//            Image(
//                painter = painterResource(R.drawable.bloom_text_logo),
//                contentDescription = "Bloom Logo",
//                modifier = Modifier.height(32.dp)
//            )

            // Space between logo and headline
            Spacer(modifier = Modifier.height(48.dp))

            // Headline
            Text(
                text = "Grow Together,\nwith Bloom.",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subheadline
            Text(
                text = "Understand your body, embrace your changes, and grow with confidence.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Get Started Button
            CustomButton(
                text = "Let's Get Started",
//                containerColor = Secondary,
//                shadowColor = Color(0xFF4CA789),
                onClick = onGetStartedClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
