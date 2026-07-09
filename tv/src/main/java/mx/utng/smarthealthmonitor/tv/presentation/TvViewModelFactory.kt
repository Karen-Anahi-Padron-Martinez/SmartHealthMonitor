package mx.utng.smarthealthmonitor.tv.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository

class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            SmartHealthRepository.init(context)
            @Suppress("UNCHECKED_CAST")
            return TvViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
