package mx.utng.smarthealthmonitor.tv.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState
import mx.utng.smarthealthmonitor.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smarthealthmonitor.mqtt.TvMessage

class TvViewModel(
    private val context: Context
) : ViewModel() {

    private val neonRepo = TvNeonRepository()
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // Flow de mensajes MQTT entrantes
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    init {
        mqttSubscriber.connect()
        cargarDatos()

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

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lecturasDto = neonRepo.obtenerHistorialCompleto(50)
                val statsDto = neonRepo.obtenerEstadisticas()
                
                // Consultas Avanzadas
                val alertasDto = neonRepo.obtenerAlertas24h()
                val promedioHoraDto = neonRepo.obtenerPromedioPorHora()
                val recienteDispositivoDto = neonRepo.obtenerRecientePorDispositivo()
                val taquicardiaDto = neonRepo.obtenerTaquicardia()

                _state.update { it.copy(
                    lecturas = lecturasDto.map { dto -> dto.toLecturaFC() },
                    estadisticas = statsDto.map { dto -> dto.toLecturaFC() },
                    alertas24h = alertasDto.map { dto -> dto.toLecturaFC() },
                    promediosPorHora = promedioHoraDto.map { dto -> dto.toLecturaFC() },
                    recientesPorDispositivo = recienteDispositivoDto.map { dto -> dto.toLecturaFC() },
                    taquicardia = taquicardiaDto.map { dto -> dto.toLecturaFC() },
                    isLoading = false,
                    error = null
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun refresh() = cargarDatos()

    override fun onCleared() {
        mqttSubscriber.disconnect()
    }
}
