// ui/screens/SmartHealthScreen.kt
package mx.utng.kapm.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.utng.kapm.smarthealthmonitor.data.models.MockData
import mx.utng.kapm.smarthealthmonitor.ui.components.TarjetaDato
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)  // ← Agregar esto
@Composable
fun SmartHealthScreen(isLandscape: Boolean) {
    val pasosFormateados = NumberFormat.getNumberInstance(Locale.getDefault())
        .format(MockData.pasosActual)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Health Monitor") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                TarjetaDato(
                    valor = MockData.fcActual.toString(),
                    unidad = "bpm",
                    label = "Frecuencia cardíaca",
                    colorValor = if (MockData.fcActual in 60..100)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.error,
                    icono = Icons.Default.Favorite,
                    modifier = Modifier.weight(1f)
                )

                TarjetaDato(
                    valor = pasosFormateados,
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary,
                    icono = Icons.Default.DirectionsWalk,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TarjetaDato(
                    valor = MockData.fcActual.toString(),
                    unidad = "bpm",
                    label = "Frecuencia cardíaca",
                    colorValor = if (MockData.fcActual in 60..100)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.error,
                    icono = Icons.Default.Favorite
                )

                TarjetaDato(
                    valor = pasosFormateados,
                    unidad = "pasos",
                    label = "Pasos del día",
                    colorValor = MaterialTheme.colorScheme.primary,
                    icono = Icons.Default.DirectionsWalk
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Últimas lecturas:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                MockData.historialFC.take(3).forEach { lectura ->
                    Text(
                        text = "${lectura.hora} - ${lectura.valorBpm} bpm ${if (!lectura.esNormal) "⚠️" else "✅"}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        }
    }
}