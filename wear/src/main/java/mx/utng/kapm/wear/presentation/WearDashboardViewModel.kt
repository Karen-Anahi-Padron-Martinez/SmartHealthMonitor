package mx.utng.kapm.wear.presentation



import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.utng.kapm.wear.presentation.LecturaFC


// Singleton para compartir FC entre MainActivity y los Composables

object WearHealthState {
    val fc: MutableStateFlow<Int> = MutableStateFlow(72)
    val historial: MutableStateFlow<List<LecturaFC>> = MutableStateFlow(emptyList())
}

class WearDashboardViewModel : ViewModel() {
    val fc: StateFlow<Int> = WearHealthState.fc.asStateFlow()
    val historial: StateFlow<List<LecturaFC>> = WearHealthState.historial.asStateFlow()
}