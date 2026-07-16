import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 23
        targetSdk = 35
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

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Leanback Library — el estándar de Android TV
    implementation("androidx.leanback:leanback:1.2.0")
    // Glide para cargar imágenes en las cards
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room Database dependencies for shared database sources
    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // ViewModel + Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    // Fragment KTX para el delegado viewModels()
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    // Jetpack Compose BoM & Core UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")
    
    // Lifecycle Compose utilities (for collectAsStateWithLifecycle)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Compose for TV
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0")

    // Media3 / ExoPlayer
    val media3Version = "1.2.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

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

val copyLauncherResources by tasks.registering(Copy::class) {
    from("../app/src/main/res") {
        include("drawable/ic_launcher_background.xml")
        include("drawable/ic_launcher_foreground.xml")
        include("values/ic_launcher_background.xml")
    }
    into("src/main/res")
}

tasks.configureEach {
    if (name != "copyLauncherResources" && name != "clean" && !name.startsWith("preBuild")) {
        dependsOn(copyLauncherResources)
    }
}

tasks.clean {
    delete("src/main/java/mx/utng/kapm/smarthealthmonitor/data")
    delete("src/main/res/drawable/ic_launcher_background.xml")
    delete("src/main/res/drawable/ic_launcher_foreground.xml")
    delete("src/main/res/values/ic_launcher_background.xml")
}
