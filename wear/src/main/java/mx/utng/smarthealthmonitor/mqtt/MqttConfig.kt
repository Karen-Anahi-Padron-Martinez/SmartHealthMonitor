package mx.utng.smarthealthmonitor.mqtt

import mx.utng.kapm.wear.BuildConfig
 
object MqttConfig {
    val BROKER_URL  = BuildConfig.MQTT_BROKER_URL
    val USERNAME    = BuildConfig.MQTT_USERNAME
    val PASSWORD    = BuildConfig.MQTT_PASSWORD
 
    // Topics del proyecto
    const val TOPIC_FC    = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV    = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"
 
    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1
 
    // Client IDs únicos por dispositivo
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP  = "smarthealthmonitor-app"
    const val CLIENT_TV   = "smarthealthmonitor-tv"
}
