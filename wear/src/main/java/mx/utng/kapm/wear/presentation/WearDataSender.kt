package mx.utng.kapm.wear.presentation

import android.content.Context
import android.util.Log

class WearDataSender(private val context: Context) {
    suspend fun enviarFC(bpm: Int) {
        // Temporal: Solo imprime en consola para que no falle la compilación
        Log.d("WearDataSender", "Enviando pulsaciones al teléfono: $bpm BPM")
    }
}