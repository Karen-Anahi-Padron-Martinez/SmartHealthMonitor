plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
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
}

val copySharedDatabaseFiles by tasks.registering(Copy::class) {
    from("../app/src/main/java/mx/utng/kapm/smarthealthmonitor/data") {
        include("bd/**")
        include("db/**")
        include("SmartHealthRepository.kt")
        include("MockData.kt")
    }
    into("src/main/java/mx/utng/kapm/smarthealthmonitor/data")
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
    if (name != "copySharedDatabaseFiles" && name != "copyLauncherResources" && name != "clean" && !name.startsWith("preBuild")) {
        dependsOn(copySharedDatabaseFiles)
        dependsOn(copyLauncherResources)
    }
}

tasks.clean {
    delete("src/main/java/mx/utng/kapm/smarthealthmonitor/data")
    delete("src/main/res/drawable/ic_launcher_background.xml")
    delete("src/main/res/drawable/ic_launcher_foreground.xml")
    delete("src/main/res/values/ic_launcher_background.xml")
}
