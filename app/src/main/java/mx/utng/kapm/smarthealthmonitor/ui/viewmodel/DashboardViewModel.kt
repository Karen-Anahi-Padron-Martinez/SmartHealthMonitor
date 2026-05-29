package mx.utng.kapm.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.kapm.smarthealthmonitor.data.MockData
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository

/**
 * ViewModel que conecta el repositorio con la UI.
 * Si no hay datos del wearable (valor 0), usa MockData como fallback.
 */
class DashboardViewModel : ViewModel() {

    // FC: viene del wearable real vía Repository.
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.pasosActual
        )

    // ⭐ RETO ADICIONAL: SpO2
    val spo2: StateFlow<Int> = SmartHealthRepository.spo2Flow
        .map { if (it == 0) MockData.spo2Actual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.spo2Actual
        )

    // ⭐ RETO ADICIONAL: Temperatura
    val temperatura: StateFlow<Double> = SmartHealthRepository.temperaturaFlow
        .map { if (it == 0.0) MockData.temperaturaActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.temperaturaActual
        )

    val historial = MockData.historialFC  // TODO S7: Room
}