package mx.utng.kapm.wear.presentation.watchface

import android.content.Context
import android.graphics.*
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder, currentUserStyleRepository, watchState,
    CanvasType.HARDWARE, interactiveDrawModeUpdateDelayMillis
) {
    private val paintHora = Paint().apply {
        color = Color.WHITE; textSize = 72f; isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private val paintFC = Paint().apply {
        color = Color.RED; textSize = 30f; isAntiAlias = true
    }
    private val paintSub = Paint().apply {
        color = Color.GRAY; textSize = 22f; isAntiAlias = true
    }

    override suspend fun createSharedAssets(): SharedAssets =
        object : SharedAssets { override fun onDestroy() {} }()

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        canvas.drawColor(Color.BLACK)
        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        val hora = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)
        val tw   = paintHora.measureText(hora)
        canvas.drawText(hora, cx - tw / 2, cy - 10f, paintHora)

        val seg = String.format("%02d", zonedDateTime.second)
        canvas.drawText(seg, cx - 18f, cy + 30f, paintSub)

        val fc = WearHealthState.fc.value
        if (fc > 0) {
            val fcStr = "\u2764 $fc bpm"
            val fcW   = paintFC.measureText(fcStr)
            canvas.drawText(fcStr, cx - fcW / 2, cy + 70f, paintFC)
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas, bounds: Rect,
        zonedDateTime: ZonedDateTime, sharedAssets: SharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }
}