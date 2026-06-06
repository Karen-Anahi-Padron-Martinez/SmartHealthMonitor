package mx.utng.kapm.wear.presentation

import android.content.Context
import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import androidx.health.services.client.HealthServices
import kotlinx.coroutines.*

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
        Log.d("HealthDataService", "Servicio creado")
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        // Procesar frecuencia cardíaca
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                Log.d("HealthDataService", "FC recibida: $bpm bpm")
                scope.launch {
                    wearDataSender.enviarFC(bpm)
                }
            }
        }

        // Procesar pasos diarios (reto adicional)
        val stepsDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
        stepsDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Long>) {
                val steps = dataPoint.value
                Log.d("HealthDataService", "Pasos recibidos: $steps")
                scope.launch {
                    wearDataSender.enviarPasos(steps)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d("HealthDataService", "Servicio destruido")
    }

    companion object {
        private const val TAG = "HealthDataService"

        suspend fun registrar(context: Context) {
            try {
                val hsClient = HealthServices.getClient(context)
                val passiveClient = hsClient.passiveMonitoringClient

                val config = PassiveListenerConfig.builder()
                    .setDataTypes(setOf(
                        DataType.HEART_RATE_BPM,
                        DataType.STEPS_DAILY  // Para el reto adicional
                    ))
                    .setShouldUserActivityInfoBeRequested(true)
                    .build()

                passiveClient.setPassiveListenerServiceAsync(
                    HealthDataService::class.java,
                    config
                ).await()

                Log.d(TAG, "Health Services registrado exitosamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error registrando Health Services: ${e.message}")
            }
        }
    }
}