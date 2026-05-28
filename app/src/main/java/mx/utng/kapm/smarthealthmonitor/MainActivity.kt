// MainActivity.kt
package mx.utng.kapm.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.utng.kapm.smarthealthmonitor.navigation.SmartHealthNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // NavGraph es ahora el punto de entrada — no LoginScreen directamente
            SmartHealthNavGraph()
        }
    }
}