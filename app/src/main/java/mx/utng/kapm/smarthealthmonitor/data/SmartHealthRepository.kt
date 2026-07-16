package mx.utng.kapm.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFC
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.kapm.smarthealthmonitor.data.db.SmartHealthDB

object SmartHealthRepository {

    val fcFlow = MutableStateFlow(0)

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    private val _temperaturaFlow = MutableStateFlow(0.0)
    val temperaturaFlow: StateFlow<Double> = _temperaturaFlow.asStateFlow()

    private var dao: LecturaFCDao? = null
    var syncRepository: SyncRepository? = null
        private set

    private val repositoryScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var simulationJob: Job? = null

    fun init(context: Context) {
        val db = SmartHealthDB.getDatabase(context)
        dao = db.lecturaDao()
        syncRepository = SyncRepository(dao!!)
        startSimulation()
    }

    fun startSimulation() {
        if (simulationJob?.isActive == true) return
        simulationJob = repositoryScope.launch {
            // Seed initial values if they are 0
            if (fcFlow.value == 0) fcFlow.value = MockData.fcActual
            if (_pasosFlow.value == 0) _pasosFlow.value = MockData.pasosActual
            if (_spo2Flow.value == 0) _spo2Flow.value = MockData.spo2Actual
            if (_temperaturaFlow.value == 0.0) _temperaturaFlow.value = MockData.temperaturaActual

            while (isActive) {
                delay(3000)

                // Heart rate fluctuates
                val currentFc = fcFlow.value
                val deltaFc = (-3..3).random()
                val newFc = (currentFc + deltaFc).coerceIn(60, 110)
                actualizarFC(newFc)

                // Steps increment
                val currentPasos = _pasosFlow.value
                val deltaPasos = (5..15).random()
                actualizarPasos(currentPasos + deltaPasos)

                // SpO2 fluctuates slightly
                val currentSpo2 = _spo2Flow.value
                val deltaSpo2 = (-1..1).random()
                val newSpo2 = (currentSpo2 + deltaSpo2).coerceIn(95, 100)
                actualizarSpO2(newSpo2)

                // Temperature fluctuates slightly
                val currentTemp = _temperaturaFlow.value
                val deltaTemp = ((-2..2).random() * 0.1)
                val newTemp = (currentTemp + deltaTemp).coerceIn(36.0, 37.5)
                val roundedTemp = Math.round(newTemp * 10.0) / 10.0
                actualizarTemperatura(roundedTemp)
            }
        }
    }

    fun stopSimulation() {
        simulationJob?.cancel()
    }

    suspend fun actualizarFC(bpm: Int) {
        fcFlow.value = bpm
        val estado = if (bpm in 60..100) "Normal" else if (bpm < 60) "FC Baja" else "FC Alta"
        val hora = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        syncRepository?.insertarLectura(
            LecturaFC(bpm = bpm, estado = estado, dispositivo = "app", hora = hora)
        )
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