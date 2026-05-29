package mx.utng.kapm.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.kapm.smarthealthmonitor.BuildConfig
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.kapm.smarthealthmonitor.ui.viewmodel.DashboardViewModel
import kotlin.random.Random
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val spo2 by viewModel.spo2.collectAsState()
    val temperatura by viewModel.temperatura.collectAsState()
    val historial = viewModel.historial

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SmartHealth Monitor",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onAlertClick) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Alertas"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título de métricas
            item {
                Text(
                    "Métricas en Tiempo Real",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Tarjeta FC (Frecuencia Cardíaca)
            item {
                TarjetaDato(
                    icono = Icons.Default.Favorite,
                    titulo = "Frecuencia Cardíaca",
                    valor = "$fc",
                    unidad = "bpm",
                    colorIcono = Color(0xFFE53935),
                    colorValor = MaterialTheme.colorScheme.primary
                )
            }

            // Tarjeta Pasos
            item {
                TarjetaDato(
                    icono = Icons.Default.DirectionsWalk,
                    titulo = "Pasos",
                    valor = "$pasos",
                    unidad = "pasos",
                    colorIcono = Color(0xFF43A047),
                    colorValor = MaterialTheme.colorScheme.secondary
                )
            }

            // ⭐ RETO ADICIONAL: Tarjeta SpO2
            item {
                TarjetaDato(
                    icono = Icons.Default.Air,
                    titulo = "Saturación O2",
                    valor = "$spo2",
                    unidad = "%",
                    colorIcono = Color(0xFF1E88E5),
                    colorValor = MaterialTheme.colorScheme.tertiary
                )
            }

            // ⭐ RETO ADICIONAL: Tarjeta Temperatura
            item {
                TarjetaDato(
                    icono = Icons.Default.Thermostat,
                    titulo = "Temperatura",
                    valor = String.format("%.1f", temperatura),
                    unidad = "°C",
                    colorIcono = Color(0xFFFF8F00),
                    colorValor = Color(0xFFEF6C00)
                )
            }

            // Título historial
            item {
                Text(
                    "Historial Reciente",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Lista de historial
            items(historial) { registro ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = registro.hora,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${registro.fc} bpm",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // ⭐ BOTÓN DE SIMULACIÓN PARA PRUEBAS ⭐
            item {
                if (BuildConfig.DEBUG) {
                    OutlinedButton(
                        onClick = {
                            // Simular lectura del wearable
                            val fcSimulado = Random.nextInt(60, 111)  // 60-110
                            val pasosSimulado = Random.nextInt(3000, 8001)  // 3000-8000
                            val spo2Simulado = Random.nextInt(94, 101)  // 94-100
                            val tempSimulado = Random.nextInt(360, 376) / 10.0  // 36.0-37.5

                            SmartHealthRepository.actualizarFC(fcSimulado)
                            SmartHealthRepository.actualizarPasos(pasosSimulado)
                            SmartHealthRepository.actualizarSpO2(spo2Simulado)
                            SmartHealthRepository.actualizarTemperatura(tempSimulado)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Watch,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🧪 Simular dato del wearable (DEBUG)")
                    }
                }
            }

            // Botón para ver historial completo
            item {
                Button(
                    onClick = onHistorialClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Timeline,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Historial Completo")
                }
            }
        }
    }
}

/**
 * Componente reutilizable para mostrar una métrica con icono.
 */
@Composable
fun TarjetaDato(
    icono: ImageVector,
    titulo: String,
    valor: String,
    unidad: String,
    colorIcono: Color,
    colorValor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = colorIcono.copy(alpha = 0.15f)
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    tint = colorIcono
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texto de la métrica
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = valor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorValor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unidad,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}