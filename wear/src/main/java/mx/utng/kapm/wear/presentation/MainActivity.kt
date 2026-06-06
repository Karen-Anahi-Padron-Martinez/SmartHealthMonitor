package mx.utng.kapm.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registrar Health Services
        lifecycleScope.launch {
            HealthDataService.registrar(applicationContext)
        }

        setContent {
            WearApp()
        }
    }
}

@androidx.compose.runtime.Composable
fun WearApp() {
    androidx.wear.compose.material.MaterialTheme {
        androidx.wear.compose.material.Text("Monitoreo FC Activo")
    }
}