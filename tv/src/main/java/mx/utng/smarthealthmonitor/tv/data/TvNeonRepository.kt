package mx.utng.smarthealthmonitor.tv.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.tv.data.remote.NeonClient
import mx.utng.smarthealthmonitor.tv.data.remote.NeonRequest
import mx.utng.smarthealthmonitor.tv.data.remote.LecturaFcDto

class TvNeonRepository {

    /** Obtener historial completo de los 3 dispositivos */
    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT id, bpm, estado, dispositivo, hora, created_at
                               FROM lecturas_fc
                               ORDER BY created_at DESC
                               LIMIT $1""".trimIndent(),
                    params = listOf(limite)
                )
            ).rows
        }

    /** Estadísticas / promedio por dispositivo */
    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT 0 AS id,
                               ROUND(AVG(bpm))::integer AS bpm,
                               'Promedio' AS estado,
                               dispositivo,
                               MAX(hora) AS hora
                               FROM lecturas_fc
                               GROUP BY dispositivo""".trimIndent()
                )
            ).rows
        }

    // ── CONSULTAS AVANZADAS (Reto Extra) ───────────────────

    /** Alertas de FC fuera de rango (últimas 24 horas) */
    suspend fun obtenerAlertas24h(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT id, bpm, estado, dispositivo, hora, created_at
                               FROM lecturas_fc
                               WHERE (bpm < 60 OR bpm > 100)
                                 AND created_at > NOW() - INTERVAL '24 hours'
                               ORDER BY created_at DESC""".trimIndent()
                )
            ).rows
        }

    /** Promedio de FC por hora del día */
    suspend fun obtenerPromedioPorHora(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT 0 AS id,
                               ROUND(AVG(bpm))::integer AS bpm,
                               'Promedio Hora ' || EXTRACT(HOUR FROM created_at) AS estado,
                               'promedio' AS dispositivo,
                               EXTRACT(HOUR FROM created_at) || ':00' AS hora
                               FROM lecturas_fc
                               GROUP BY EXTRACT(HOUR FROM created_at)
                               ORDER BY EXTRACT(HOUR FROM created_at)""".trimIndent()
                )
            ).rows
        }

    /** Lectura más reciente de cada dispositivo */
    suspend fun obtenerRecientePorDispositivo(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT DISTINCT ON (dispositivo)
                               id, bpm, estado, dispositivo, hora, created_at
                               FROM lecturas_fc
                               ORDER BY dispositivo, created_at DESC""".trimIndent()
                )
            ).rows
        }

    /** Detección de taquicardia sostenida (>100 bpm por 3+ lecturas seguidas) */
    suspend fun obtenerTaquicardia(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query  = """SELECT 0 AS id,
                               COUNT(*)::integer AS bpm,
                               'Taquicardia (>100 bpm)' AS estado,
                               'sostenida' AS dispositivo,
                               MIN(hora) || ' - ' || MAX(hora) AS hora
                               FROM lecturas_fc
                               WHERE bpm > 100
                                 AND created_at > NOW() - INTERVAL '1 hour'""".trimIndent()
                )
            ).rows
        }
}
