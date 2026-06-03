package mx.utng.kapm.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registrar el listener de Health Services al iniciar la app en el reloj
        lifecycleScope.launch {
            try {
                HealthDataService.registrar(applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}