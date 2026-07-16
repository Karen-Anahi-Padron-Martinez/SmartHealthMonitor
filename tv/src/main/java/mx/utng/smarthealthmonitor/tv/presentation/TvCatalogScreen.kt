package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize().background(Color(0xFF0D1B4A))) {

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
            return@Box
        }

        TvLazyColumn(
            modifier = Modifier.fillMaxSize().padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Cabecera con Botón de Actualizar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SmartHealth TV Dashboard", 
                             style = MaterialTheme.typography.headlineLarge, 
                             color = Color.White, 
                             fontWeight = FontWeight.Bold)
                        if (state.error != null) {
                            Text("Error: ${state.error}", 
                                 color = Color.Red, 
                                 style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Button(
                        onClick = { viewModel.refresh() },
                        colors = ButtonDefaults.colors(
                            containerColor = Color(0xFF1565C0),
                            focusedContainerColor = Color(0xFF42A5F5)
                        )
                    ) {
                        Text("↺ Actualizar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Fila 1: Estado Actual (3 dispositivos)
            item {
                RowSection(title = "⚡ Estado Actual (3 dispositivos)") {
                    if (state.recientesPorDispositivo.isEmpty()) {
                        Text("No hay datos recientes disponibles", color = Color.White.copy(0.6f))
                    } else {
                        TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.recientesPorDispositivo) { lectura ->
                                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                            }
                        }
                    }
                }
            }

            // Fila 2: Historial Completo (últimas 50 lecturas combinadas)
            item {
                RowSection(title = "📋 Historial Completo (Últimas 50 lecturas)") {
                    if (state.lecturas.isEmpty()) {
                        Text("No hay historial disponible", color = Color.White.copy(0.6f))
                    } else {
                        TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.lecturas) { lectura ->
                                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                            }
                        }
                    }
                }
            }

            // Fila 3: Consultas Avanzadas y Estadísticas (Reto Extra)
            item {
                val advancedItems = remember(state) {
                    state.estadisticas + state.alertas24h + state.promediosPorHora + state.taquicardia
                }
                RowSection(title = "📈 Consultas Avanzadas y Estadísticas") {
                    if (advancedItems.isEmpty()) {
                        Text("No hay datos avanzados/estadísticos", color = Color.White.copy(0.6f))
                    } else {
                        TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(advancedItems) { lectura ->
                                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun RowSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge,
             color = Color.White, fontWeight = FontWeight.Bold)
        content()
    }
}
