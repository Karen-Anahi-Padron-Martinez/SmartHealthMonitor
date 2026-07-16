import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "mx.utng.kapm.smarthealthmonitor"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "mx.utng.kapm.smarthealthmonitor"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Leer credenciales de local.properties
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            val stream = localPropertiesFile.inputStream()
            properties.load(stream)
            stream.close()
        }
        val brokerUrl = properties.getProperty("mqtt.broker_url") ?: "ssl://abc123def456.s1.eu.hivemq.cloud:8883"
        val username = properties.getProperty("mqtt.username") ?: "tu-usuario-hivemq"
        val password = properties.getProperty("mqtt.password") ?: "tu-contraseña-segura"

        val neonApiKey = properties.getProperty("NEON_API_KEY") ?: "placeholder_api_key"
        val neonHost = properties.getProperty("NEON_HOST") ?: "tu-host.neon.tech"
        val neonDb = properties.getProperty("NEON_DB") ?: "neondb"
        val neonUser = properties.getProperty("NEON_USER") ?: "neondb_owner"
        val neonPassword = properties.getProperty("NEON_PASSWORD") ?: "tu_password"

        buildConfigField("String", "MQTT_BROKER_URL", "\"$brokerUrl\"")
        buildConfigField("String", "MQTT_USERNAME", "\"$username\"")
        buildConfigField("String", "MQTT_PASSWORD", "\"$password\"")
        
        buildConfigField("String", "NEON_API_KEY", "\"$neonApiKey\"")
        buildConfigField("String", "NEON_HOST", "\"$neonHost\"")
        buildConfigField("String", "NEON_DB", "\"$neonDb\"")
        buildConfigField("String", "NEON_USER", "\"$neonUser\"")
        buildConfigField("String", "NEON_PASSWORD", "\"$neonPassword\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.jetbrains.kotlin.reflect)
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.health.services.client)
    implementation(libs.guava)
    implementation(libs.kotlinx.coroutines.guava)
    // Room
    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Cast SDK
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")

    // Eclipse Paho MQTT y Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paho.mqtt)
    implementation(libs.paho.android.service)

    // Retrofit + OkHttp para llamadas a Neon HTTP API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // WorkManager para sync periódico en background
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}