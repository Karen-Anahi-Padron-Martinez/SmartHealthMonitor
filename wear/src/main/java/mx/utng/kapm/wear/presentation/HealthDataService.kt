package mx.utng.kapm.wear.presentation


import android.content.Context
import androidx.health.services.client.HealthServices
//import androidx.health.services.client.PassiveListenerConfig
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
//import androidx.health.services.client.setPassiveListenerServiceAsync
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender // Implementado en S6

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)

        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<*>) {
                // El sensor entrega el valor como Double
                val bpm = (dataPoint.value as Double).toInt()
                scope.launch {
                    wearDataSender.enviarFC(bpm)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        suspend fun registrar(context: Context) {
            val hsClient = HealthServices.getClient(context)
            val passiveClient = hsClient.passiveMonitoringClient

            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                .setShouldUserActivityInfoBeRequested(true)
                .build()

            passiveClient.setPassiveListenerServiceAsync(
                HealthDataService::class.java,
                config
            ).await()
        }
    }
}