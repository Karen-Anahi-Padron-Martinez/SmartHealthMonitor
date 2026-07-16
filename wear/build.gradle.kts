import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "mx.utng.kapm.wear"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "mx.utng.kapm.wear"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling)
    implementation(libs.play.services.wearable)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.health.services.client)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.guava)
    // Compose for Wear OS — ScalingLazyColumn, TimeText, PositionIndicator
    implementation("androidx.wear.compose:compose-material:1.3.1")
    implementation("androidx.wear.compose:compose-foundation:1.3.1")

// Wear OS Navigation — SwipeDismissableNavHost
    implementation("androidx.wear.compose:compose-navigation:1.3.1")

// Horologist — utilidades adicionales de Google para Wear OS
    implementation("com.google.android.horologist:horologist-compose-layout:0.6.17")
    implementation("com.google.android.horologist:horologist-compose-material:0.6.17")

// ViewModel en Wear OS
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.wear.watchface:watchface:1.2.1")
    implementation("androidx.wear.watchface:watchface-complications-rendering:1.2.1")
    implementation("androidx.wear.watchface:watchface-style:1.2.1")

    // Play services tasks support for coroutines await()
    implementation(libs.kotlinx.coroutines.play.services)
    // Compose Material for Icons support
    implementation("androidx.compose.material:material:1.7.5")
    implementation(libs.androidx.compose.material.icons.extended)

    // Eclipse Paho MQTT y Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paho.mqtt)
    implementation(libs.paho.android.service)

    // Retrofit + OkHttp para llamadas a Neon HTTP API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}