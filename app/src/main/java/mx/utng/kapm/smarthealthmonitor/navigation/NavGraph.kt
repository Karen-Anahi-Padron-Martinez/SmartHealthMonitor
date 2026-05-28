// navigation/NavGraph.kt
package mx.utng.kapm.smarthealthmonitor.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.kapm.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.kapm.smarthealthmonitor.ui.screens.LoginScreen
import mx.utng.kapm.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun SmartHealthNavGraph() {
    SmartHealthMonitorTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            // ── Login ──────────────────────────────────────
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true  // eliminar Login del back stack
                            }
                        }
                    }
                )
            }
            // ── Dashboard ──────────────────────────────────
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onHistorialClick = {
                        navController.navigate(Screen.Historial.route)
                    },
                    onAlertClick = {
                        navController.navigate(Screen.Alerta.route)
                    }
                )
            }
            // ── Historial ──────────────────────────────────
            composable(Screen.Historial.route) {
                PantallaEnConstruccion(
                    titulo = "Historial completo",
                    onBack = { navController.popBackStack() }
                )
            }
            // ── Alerta ─────────────────────────────────────
            composable(Screen.Alerta.route) {
                PantallaEnConstruccion(
                    titulo = "Enviar alerta",
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

// Pantalla temporal para destinos no implementados aún
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "🚧",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    "Próximamente:",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Esta pantalla está en construcción",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Pantalla en construcción")
@Composable
private fun PantallaEnConstruccionPreview() {
    SmartHealthMonitorTheme {
        PantallaEnConstruccion(
            titulo = "Historial completo",
            onBack = {}
        )
    }
}