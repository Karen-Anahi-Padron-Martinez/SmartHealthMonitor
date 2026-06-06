package mx.utng.kapm.smarthealthmonitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.kapm.smarthealthmonitor.data.MockData
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFC

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.fcActual)

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.pasosActual)

    val spo2: StateFlow<Int> = SmartHealthRepository.spo2Flow
        .map { if (it == 0) MockData.spo2Actual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.spo2Actual)

    val temperatura: StateFlow<Double> = SmartHealthRepository.temperaturaFlow
        .map { if (it == 0.0) MockData.temperaturaActual else it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MockData.temperaturaActual)

    // ← NUEVO: historial desde Room
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}