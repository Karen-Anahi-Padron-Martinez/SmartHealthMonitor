// ui/components/FilaHistorial.kt
package mx.utng.kapm.smarthealthmonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.utng.kapm.smarthealthmonitor.data.models.LecturaFC
import mx.utng.kapm.smarthealthmonitor.data.models.MockData
import mx.utng.kapm.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun FilaHistorial(
    lectura: LecturaFC,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Valor FC con color según si es normal o no
        Text(
            text = "${lectura.valorBpm} bpm",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (lectura.esNormal)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.error
        )
        Text(
            text = lectura.hora,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Preview(showBackground = true, name = "FilaHistorial - Normal")
@Composable
private fun FilaHistorialNormalPreview() {
    SmartHealthMonitorTheme {
        FilaHistorial(lectura = LecturaFC(1, 78, "11:00", true))
    }
}

@Preview(showBackground = true, name = "FilaHistorial - Anormal")
@Composable
private fun FilaHistorialAnormalPreview() {
    SmartHealthMonitorTheme {
        FilaHistorial(lectura = LecturaFC(4, 95, "09:30", false))
    }
}