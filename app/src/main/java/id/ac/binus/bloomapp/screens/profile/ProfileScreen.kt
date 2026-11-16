package id.ac.binus.bloomapp.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import id.ac.binus.bloomapp.ui.theme.Outline
import id.ac.binus.bloomapp.ui.theme.Primary
import id.ac.binus.bloomapp.ui.theme.PrimaryLight
import id.ac.binus.bloomapp.ui.theme.Surface
import id.ac.binus.bloomapp.ui.theme.TextPrimary
import id.ac.binus.bloomapp.ui.theme.TextSecondary

@Composable
fun ProfileScreen(onSignedOut: () -> Unit) {
    val user = remember { FirebaseAuth.getInstance().currentUser }
    val name = user?.displayName?.takeIf { it.isNotBlank() } ?: "Bloom friend"
    val email = user?.email ?: "Belum ada email"
    val initials = name.trim().split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString("").uppercase()

    Column(
        modifier = Modifier.fillMaxSize().background(Surface).padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = PrimaryLight,
            border = androidx.compose.foundation.BorderStroke(1.dp, Outline)
        ) {
            Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(68.dp).clip(CircleShape).background(Primary), contentAlignment = Alignment.Center) {
                    if (user?.photoUrl != null) {
                        AsyncImage(user.photoUrl, contentDescription = "Profile photo", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Text(initials.ifBlank { "B" }, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.size(16.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(email, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = { FirebaseAuth.getInstance().signOut(); onSignedOut() },
            modifier = Modifier.fillMaxWidth().height(52.dp).shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Icon(Icons.Outlined.ExitToApp, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Sign out", fontWeight = FontWeight.Bold)
        }
    }
}
