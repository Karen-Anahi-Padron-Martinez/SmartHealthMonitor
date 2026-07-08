package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {

    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del BrowseFragment
        title        = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    private fun observarDatos() {
        // Observar historial de Room y actualizar la fila
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    lecturas.forEach { histAdapter.add(it) }
                }
            }
        }

        // Observar frecuencia cardíaca actual en tiempo real para el estado actual
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fc.collect { fcVal ->
                    estadoAdapter.clear()
                    estadoAdapter.add(LecturaFC(id = 0, valorBpm = fcVal, hora = "Ahora"))
                    estadoAdapter.add(LecturaFC(id = 1, valorBpm = 4250, hora = "Pasos"))
                }
            }
        }
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (FC + Pasos) ───────────
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(0, "Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ────────────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial FC"), histAdapter))

        // ── Fila 3: Alertas recientes (Reto) ───────────
        val alertasAdapter = ArrayObjectAdapter(FCCardPresenter())
        alertasAdapter.add(LecturaFC(valorBpm = 145, hora = "14:15 - Taquicardia"))
        alertasAdapter.add(LecturaFC(valorBpm = 45, hora = "10:30 - Bradicardia"))
        alertasAdapter.add(LecturaFC(valorBpm = 150, hora = "08:00 - Taquicardia"))
        rowsAdapter.add(ListRow(HeaderItem(2, "Alertas recientes"), alertasAdapter))

        this.adapter = rowsAdapter
    }
}
