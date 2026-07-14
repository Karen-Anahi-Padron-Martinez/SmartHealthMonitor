package mx.utng.kapm.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import mx.utng.kapm.wear.presentation.LecturaFC
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher

// Singleton para compartir FC entre MainActivity y los Composables

object WearHealthState {
    val fc: MutableStateFlow<Int> = MutableStateFlow(72)
    val historial: MutableStateFlow<List<LecturaFC>> = MutableStateFlow(emptyList())
}

class WearDashboardViewModel(application: Application) : AndroidViewModel(application) {
    val fc: StateFlow<Int> = WearHealthState.fc.asStateFlow()
    val historial: StateFlow<List<LecturaFC>> = WearHealthState.historial.asStateFlow()

    private val mqttPublisher = MqttWearPublisher(application)

    init {
        mqttPublisher.connect()

        // Observar cambios de frecuencia y publicar vía MQTT
        viewModelScope.launch {
            WearHealthState.fc.collect { bpm ->
                val estado = when {
                    bpm < 60 -> "FC Baja"
                    bpm > 100 -> "FC Alta"
                    else -> "Normal"
                }
                mqttPublisher.publishFC(bpm, estado)
            }
        }

        viewModelScope.launch {
            // Seed initial history if empty
            if (WearHealthState.historial.value.isEmpty()) {
                val baseTime = System.currentTimeMillis()
                val seedList = List(5) { i ->
                    val bpm = (70..85).random()
                    LecturaFC(
                        valorBpm = bpm,
                        timestamp = baseTime - (5 - i) * 60000
                    )
                }
                WearHealthState.historial.value = seedList
            }

            while (isActive) {
                delay(3000)
                val currentFc = WearHealthState.fc.value
                val deltaFc = (-3..3).random()
                val newFc = (currentFc + deltaFc).coerceIn(60, 110)
                WearHealthState.fc.value = newFc

                // Periodically add to mock history list
                val currentList = WearHealthState.historial.value.toMutableList()
                currentList.add(LecturaFC(valorBpm = newFc))
                if (currentList.size > 10) {
                    currentList.removeAt(0)
                }
                WearHealthState.historial.value = currentList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}