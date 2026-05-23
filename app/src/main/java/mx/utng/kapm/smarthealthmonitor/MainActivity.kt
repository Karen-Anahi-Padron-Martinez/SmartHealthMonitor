package mx.utng.kapm.smarthealthmonitor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import mx.utng.kapm.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHealthMonitorTheme {
                // Estado de navegación: true = Dashboard, false = Login
                var isLoggedIn by remember { mutableStateOf(false) }

                if (isLoggedIn) {
                    // Pantalla principal después del login
                    val configuration = LocalConfiguration.current
                    val isLandscape = configuration.orientation ==
                            android.content.res.Configuration.ORIENTATION_LANDSCAPE

                    SmartHealthScreen(isLandscape = isLandscape)
                } else {
                    // Pantalla de login
                    Surface(modifier = Modifier.fillMaxSize()) {
                        LoginScreen(
                            onLoginSuccess = {
                                Log.d("SmartHealth", "Login exitoso")
                                isLoggedIn = true // Cambia al Dashboard
                            }
                        )
                    }
                }
            }
        }
    }
}