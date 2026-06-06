package mx.utng.kapm.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFC
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.kapm.smarthealthmonitor.data.db.SmartHealthDB

object SmartHealthRepository {

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    private val _temperaturaFlow = MutableStateFlow(0.0)
    val temperaturaFlow: StateFlow<Double> = _temperaturaFlow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun actualizarSpO2(spo2: Int) {
        _spo2Flow.value = spo2
    }

    fun actualizarTemperatura(temp: Double) {
        _temperaturaFlow.value = temp
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}