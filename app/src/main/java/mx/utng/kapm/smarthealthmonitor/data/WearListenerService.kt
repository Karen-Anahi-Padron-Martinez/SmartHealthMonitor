package mx.utng.kapm.smarthealthmonitor.data

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Servicio que escucha mensajes del wearable vía Wearable Data Layer API.
 * Recibe FC, Pasos, SpO2 y Temperatura desde el reloj Wear OS.
 */
class WearListenerService : WearableListenerService() {

    // Scope propio del servicio para las corrutinas de Room
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val PATH_FC = "/smarthealthmonitor/fc"
        const val PATH_PASOS = "/smarthealthmonitor/pasos"
        const val PATH_SPO2 = "/smarthealthmonitor/spo2"
        const val PATH_TEMPERATURA = "/smarthealthmonitor/temperatura"
        private const val TAG = "WearListener"
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = String(messageEvent.data)
        val path = messageEvent.path
        Log.d(TAG, "📥 Mensaje recibido: path=$path, data=$data")

        when (path) {
            PATH_FC -> {
                val bpm = data.toIntOrNull()
                if (bpm != null) {
                    // ← suspend: necesita corrutina porque ahora guarda en Room
                    serviceScope.launch {
                        SmartHealthRepository.actualizarFC(bpm)
                    }
                    Log.i(TAG, "❤️ FC actualizada: $bpm bpm")
                } else {
                    Log.e(TAG, "❌ FC inválida: $data")
                }
            }

            PATH_PASOS -> {
                val pasos = data.toIntOrNull()
                if (pasos != null) {
                    SmartHealthRepository.actualizarPasos(pasos)
                    Log.i(TAG, "👣 Pasos actualizados: $pasos")
                } else {
                    Log.e(TAG, "❌ Pasos inválidos: $data")
                }
            }

            PATH_SPO2 -> {
                val spo2 = data.toIntOrNull()
                if (spo2 != null && spo2 in 70..100) {
                    SmartHealthRepository.actualizarSpO2(spo2)
                    Log.i(TAG, "🫁 SpO2 actualizada: $spo2%")
                } else {
                    Log.e(TAG, "❌ SpO2 inválida: $data")
                }
            }

            PATH_TEMPERATURA -> {
                val temp = data.toDoubleOrNull()
                if (temp != null && temp in 35.0..42.0) {
                    SmartHealthRepository.actualizarTemperatura(temp)
                    Log.i(TAG, "🌡️ Temperatura actualizada: $temp°C")
                } else {
                    Log.e(TAG, "❌ Temperatura inválida: $data")
                }
            }

            else -> Log.w(TAG, "⚠️ Path desconocido: $path")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelar el scope cuando el servicio muere
        serviceScope.coroutineContext[SupervisorJob()]?.cancel()
    }
}