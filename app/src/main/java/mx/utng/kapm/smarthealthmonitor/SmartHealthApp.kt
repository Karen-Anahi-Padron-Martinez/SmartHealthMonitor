package mx.utng.kapm.smarthealthmonitor

import android.app.Application
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
    }
}