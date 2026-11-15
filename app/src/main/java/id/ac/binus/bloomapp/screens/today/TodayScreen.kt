package id.ac.binus.bloomapp.screens.today

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.ac.binus.bloomapp.ui.theme.*

@Composable
fun TodayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary
        )
    }
}