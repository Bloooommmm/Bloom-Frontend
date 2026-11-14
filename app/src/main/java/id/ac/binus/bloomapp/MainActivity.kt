package id.ac.binus.bloomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.ac.binus.bloomapp.navigation.AppNavigation
import id.ac.binus.bloomapp.ui.theme.BloomAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BloomAppTheme {
                AppNavigation()
            }
        }
    }
}