package id.ac.binus.bloomapp.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import id.ac.binus.bloomapp.R
import id.ac.binus.bloomapp.ui.theme.*
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.tasks.await
import id.ac.binus.bloomapp.data.network.BloomApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var signingIn by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var loginError by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        signingIn = false
        if (result.resultCode == Activity.RESULT_CANCELED && result.data == null) return@rememberLauncherForActivityResult
        runCatching {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            signingIn = true
            loginError = null
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { authTask ->
                if (!authTask.isSuccessful) {
                    signingIn = false
                    loginError = "Sign-in failed. Try again."
                    return@addOnCompleteListener
                }

                // Home should not wait for a backend round-trip; profile sync recovers in background.
                onGoogleSignInClick()
                scope.launch {
                    try {
                        val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
                            ?: error("Missing Firebase token")
                        withTimeout(8_000) { BloomApi().syncCurrentUser(token) }
                    } catch (_: Exception) {
                        // Backend sync retries on the next authenticated request.
                    } finally {
                        signingIn = false
                    }
                }
            }
        }.onFailure { error ->
            signingIn = false
            loginError = if ((error as? ApiException)?.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) null
                else "Couldn't sign in. Check your connection and try again."
        }
    }
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Your new journey starts here.",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.fillMaxWidth()
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
                onClick = {
                    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(id.ac.binus.bloomapp.R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    loginError = null
                    launcher.launch(GoogleSignIn.getClient(context, options).signInIntent)
                },
                enabled = !signingIn,
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
                    text = if (signingIn) "Signing in..." else "Continue with Google",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF53575B)
                )
            }

            loginError?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Disclaimer
            androidx.compose.foundation.text.BasicText(
                text = androidx.compose.ui.text.AnnotatedString.Builder().apply {
                    append("By continuing, you agree to our ")
                    pushStyle(androidx.compose.ui.text.SpanStyle(textDecoration = TextDecoration.Underline))
                    append("Terms of Service")
                    pop()
                    append(" and ")
                    pushStyle(androidx.compose.ui.text.SpanStyle(textDecoration = TextDecoration.Underline))
                    append("Privacy Policy")
                    pop()
                    append(".")
                }.toAnnotatedString(),
                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary, textAlign = TextAlign.Center),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
