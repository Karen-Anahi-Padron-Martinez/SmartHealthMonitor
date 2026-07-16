package mx.utng.smarthealthmonitor.domain.model

data class LecturaFC(
    val id: Int,
    val bpm: Int,
    val estado: String,
    val hora: String,
    val dispositivo: String = "app"
)
