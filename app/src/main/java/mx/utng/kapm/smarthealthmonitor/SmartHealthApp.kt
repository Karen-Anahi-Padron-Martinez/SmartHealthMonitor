package mx.utng.kapm.smarthealthmonitor

import android.app.Application
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.kapm.smarthealthmonitor.data.sync.NeonSyncWorker
import mx.utng.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
        
        // Programar sync periódico con Neon
        NeonSyncWorker.schedule(this)
        // Inicializar MQTT con el StateFlow del Repository
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository.fcFlow
        )
        mqttService.connect()
    }
}