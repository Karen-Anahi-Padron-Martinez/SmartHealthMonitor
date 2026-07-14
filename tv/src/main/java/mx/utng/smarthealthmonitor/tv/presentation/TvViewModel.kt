package mx.utng.smarthealthmonitor.tv.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState
import mx.utng.smarthealthmonitor.domain.model.LecturaFC as DomainLecturaFC
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smarthealthmonitor.mqtt.TvMessage

class TvViewModel(
    private val repository : SmartHealthRepository,
    private val context    : Context
) : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // Flow de mensajes MQTT entrantes
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    init {
        mqttSubscriber.connect()

        // Observar historial reactivo del Room DAO
        viewModelScope.launch {
            repository.obtenerHistorial()
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { lecturas ->
                    _state.update {
                        it.copy(
                            lecturas = lecturas.map { db ->
                                DomainLecturaFC(
                                    id = db.id,
                                    bpm = db.valorBpm,
                                    estado = if (db.esNormal) "Normal" else "Anómala",
                                    hora = db.hora
                                )
                            },
                            isLoading = false
                        )
                    }
                }
        }

        // Observar mensajes MQTT y actualizar el estado de la UI
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg ?: return@collect
                _state.update { it.copy(
                    fcActual = tvMsg.bpm,
                    fcEstado = tvMsg.estado,
                    ultimaHora = tvMsg.hora,
                    isLoading = false
                )}
            }
        }
    }

    override fun onCleared() {
        mqttSubscriber.disconnect()
    }
}
