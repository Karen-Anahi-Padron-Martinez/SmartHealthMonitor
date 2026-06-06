package mx.utng.kapm.wear.presentation

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {
    private val messageClient: MessageClient = Wearable.getMessageClient(context)

    companion object {
        const val FC_PATH = "/fc_data"
        const val STEPS_PATH = "/steps_data"
        private const val TAG = "WearDataSender"
    }

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = messageClient.connectedNodes.await()
            nodes.forEach { node ->
                val data = bpm.toString().toByteArray()
                messageClient.sendMessage(
                    node.id,
                    FC_PATH,
                    data
                ).await()
                Log.d(TAG, "FC enviada: $bpm bpm al nodo ${node.displayName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando FC: ${e.message}")
        }
    }

    suspend fun enviarPasos(steps: Long) {
        try {
            val nodes = messageClient.connectedNodes.await()
            nodes.forEach { node ->
                val data = steps.toString().toByteArray()
                messageClient.sendMessage(
                    node.id,
                    STEPS_PATH,
                    data
                ).await()
                Log.d(TAG, "Pasos enviados: $steps al nodo ${node.displayName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando pasos: ${e.message}")
        }
    }
}