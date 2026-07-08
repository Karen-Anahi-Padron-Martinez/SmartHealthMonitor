plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 21
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
