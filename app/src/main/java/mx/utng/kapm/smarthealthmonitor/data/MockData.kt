package mx.utng.kapm.smarthealthmonitor.data

/**
 * Datos mock para desarrollo cuando no hay wearable conectado.
 * Se usan como fallback en el ViewModel.
 */
object MockData {
    val fcActual = 72
    val pasosActual = 5432
    val spo2Actual = 98          // ⭐ RETO
    val temperaturaActual = 36.5  // ⭐ RETO

    val historialFC = listOf(
        HistorialFC("09:00", 72),
        HistorialFC("10:00", 75),
        HistorialFC("11:00", 78),
        HistorialFC("12:00", 74),
        HistorialFC("13:00", 80)
    )
}

data class HistorialFC(
    val hora: String,
    val fc: Int
)