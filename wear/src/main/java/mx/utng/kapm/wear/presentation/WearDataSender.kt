package mx.utng.kapm.wear.presentation


import android.content.Context
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
            nodes.forEach { node ->
                Wearable.getMessageClient(context).sendMessage(
                    node.id,
                    "/smarthealthmonitor/fc",
                    bpm.toString().toByteArray()
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}