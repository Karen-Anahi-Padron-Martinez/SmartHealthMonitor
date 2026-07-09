package mx.utng.smarthealthmonitor.tv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState
import mx.utng.smarthealthmonitor.domain.model.LecturaFC as DomainLecturaFC

class TvViewModel : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // FC actual del wearable (o 0 si no hay dato)
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    init {
        // Observar historial reactivo del Room DAO
        viewModelScope.launch {
            SmartHealthRepository.obtenerHistorial()
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
        // Observar FC actual (StateFlow del sensor)
        viewModelScope.launch {
            SmartHealthRepository.fcFlow.collect { bpm ->
                _state.update { it.copy(fcActual = bpm) }
            }
        }
    }
}
