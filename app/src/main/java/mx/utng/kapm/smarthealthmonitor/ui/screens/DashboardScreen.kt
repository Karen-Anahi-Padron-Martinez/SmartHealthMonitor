// ui/screens/DashboardScreen.kt
package mx.utng.kapm.smarthealthmonitor.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import mx.utng.kapm.smarthealthmonitor.data.models.LecturaFC
import mx.utng.kapm.smarthealthmonitor.data.models.MockData
import mx.utng.kapm.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.kapm.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.kapm.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    fc: Int = MockData.fcActual,
    pasos: Int = MockData.pasosActual,
    historial: List<LecturaFC> = MockData.historialFC
) {
    val esFCNormal = fc in 60..100

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SmartHealth",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50), // VERDE
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAlertClick,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Enviar alerta de emergencia",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // ← APLICAR paddingValues del Scaffold
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 100.dp // ← AÚN MÁS ESPACIO para el FAB
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Tarjeta FC con chip de estado ─────────
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TarjetaDato(
                        valor = "$fc",
                        unidad = "bpm",
                        label = "Frecuencia cardíaca",
                        colorValor = if (esFCNormal)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )

                    // Chip de estado de salud
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = if (esFCNormal) "Normal" else "Consulta al médico"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (esFCNormal)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (esFCNormal)
                                Color(0xFF4CAF50).copy(alpha = 0.12f)
                            else
                                MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            labelColor = if (esFCNormal)
                                Color(0xFF2E7D32)
                            else
                                MaterialTheme.colorScheme.error,
                            leadingIconContentColor = if (esFCNormal)
                                Color(0xFF4CAF50)
                            else
                                MaterialTheme.colorScheme.error
                        )
                    )
                }
            }

            // ── Tarjeta Pasos ─────────────────────────
            item {
                TarjetaDato(
                    valor = "%,d".format(pasos),
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary
                )
            }

            // ── Encabezado historial ──────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Historial reciente",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(
                        onClick = {
                            Log.d("DashboardScreen", "Ver todo clickeado")
                            onHistorialClick() // ← LLAMAR AL CALLBACK
                        }
                    ) {
                        Text("Ver todo")
                    }
                }
            }

            // ── Lista del historial ───────────────────
            items(historial, key = { it.id }) { lectura ->
                FilaHistorial(lectura = lectura)
            }

            // ── Espaciador adicional al final ─────────
            item {
                Spacer(modifier = Modifier.height(80.dp)) // ← ESPACIO EXTRA
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Dashboard - Light",
    showSystemUi = true
)
@Preview(
    showBackground = true,
    name = "Dashboard - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen(
            onHistorialClick = {
                // En preview no hace nada, pero demuestra que el callback existe
            },
            onAlertClick = {
                // En preview no hace nada, pero demuestra que el callback existe
            }
        )
    }
}