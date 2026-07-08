package mx.utng.smarthealthmonitor.tv

import android.graphics.Color
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import mx.utng.kapm.smarthealthmonitor.data.db.LecturaFC

class FCCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            // CRÍTICO: sin estas dos líneas,
            // el D-pad no puede navegar a este card
            isFocusable           = true
            isFocusableInTouchMode = true
            setMainImageDimensions(240, 180)
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
        val card    = viewHolder.view as ImageCardView
        val lectura = item as? LecturaFC ?: return

        // Detectar si es una Alerta
        if (lectura.hora.contains("Taquicardia") || lectura.hora.contains("Bradicardia") || lectura.hora.contains("Emergencia")) {
            card.titleText = "¡Alerta!"
            card.contentText = "${lectura.valorBpm} bpm - ${lectura.hora}"
            card.setBackgroundColor(Color.parseColor("#B3261E")) // Rojo error para alertas
        } else if (lectura.hora == "Pasos") {
            // Si es la card de pasos, mostrar pasos en vez de bpm
            card.titleText = "${lectura.valorBpm} pasos"
            card.contentText = lectura.hora
            card.setBackgroundColor(Color.parseColor("#1B4F8A")) // Azul primario para pasos
        } else {
            // Caso regular de frecuencia cardíaca (FC)
            card.titleText   = "${lectura.valorBpm} bpm"
            card.contentText = lectura.hora

            // Color de fondo según si FC es normal
            val bgColor = if (lectura.esNormal) {
                Color.parseColor("#1B4F8A")  // primary
            } else {
                Color.parseColor("#B3261E")  // error
            }
            card.setBackgroundColor(bgColor)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val card = viewHolder.view as ImageCardView
        card.mainImage = null
    }
}
