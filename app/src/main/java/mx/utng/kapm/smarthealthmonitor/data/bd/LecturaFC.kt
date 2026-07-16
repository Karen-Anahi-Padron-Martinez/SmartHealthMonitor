package mx.utng.kapm.smarthealthmonitor.data.db

import androidx.room.*

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id           : Int     = 0,
    val bpm          : Int,
    val estado       : String,
    val dispositivo  : String  = "app",  // wear | app | tv
    val hora         : String,
    @ColumnInfo(name = "sincronizado")
    val sincronizado : Boolean = false   // false = pendiente de sync
) {
    // Para retrocompatibilidad
    val valorBpm: Int get() = bpm
    val esNormal: Boolean get() = estado == "Normal"

    // Constructor secundario para mantener compatibilidad con el código existente
    constructor(
        id: Int = 0,
        valorBpm: Int,
        timestamp: Long = System.currentTimeMillis(),
        hora: String = java.text.SimpleDateFormat(
            "HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date()),
        esNormal: Boolean = valorBpm in 60..100
    ) : this(
        id = id,
        bpm = valorBpm,
        estado = if (esNormal) "Normal" else if (valorBpm < 60) "FC Baja" else "FC Alta",
        dispositivo = "app",
        hora = hora,
        sincronizado = false
    )
}