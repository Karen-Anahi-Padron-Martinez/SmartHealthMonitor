package mx.utng.smarthealthmonitor.tv

import android.app.Application
import mx.utng.kapm.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthTvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
    }
}
