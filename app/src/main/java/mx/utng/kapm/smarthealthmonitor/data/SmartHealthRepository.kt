package mx.utng.kapm.smarthealthmonitor.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositorio singleton que centraliza los datos de salud.
 * El WearListenerService escribe aquí.
 * El ViewModel lee de aquí.
 */
object SmartHealthRepository {

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // ⭐ RETO ADICIONAL: SpO2 (saturación de oxígeno, %)
    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    // ⭐ RETO ADICIONAL: Temperatura corporal (°C)
    private val _temperaturaFlow = MutableStateFlow(0.0)
    val temperaturaFlow: StateFlow<Double> = _temperaturaFlow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    //  RETO ADICIONAL
    fun actualizarSpO2(spo2: Int) {
        _spo2Flow.value = spo2
    }

    fun actualizarTemperatura(temp: Double) {
        _temperaturaFlow.value = temp
    }
}