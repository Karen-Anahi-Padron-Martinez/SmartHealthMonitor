package mx.edu.utng.kapm.wear.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Singleton para compartir FC entre MainActivity y los Composables
object WearHealthState {
    val fc: MutableStateFlow<Int> = MutableStateFlow(72)
}

class WearDashboardViewModel : ViewModel() {
    val fc: StateFlow<Int> = WearHealthState.fc.asStateFlow()
}