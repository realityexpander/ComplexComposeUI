plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.realityexpander.complexcomposeui"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.realityexpander.complexcomposeui"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation("androidx.graphics:graphics-shapes-android:1.0.1")
    implementation("androidx.window:window-core-android:1.4.0")
    implementation("androidx.compose.material3:material3-window-size-class-android:1.2.1") // 1.2.1 is Maximum version before update to lifecycle-livedata-2.8.3-runtime
    implementation("androidx.compose.material3.adaptive:adaptive-android:1.0.0") // 1.0.0 is Max version before update to Android API 35+, AGP
    implementation("androidx.compose.material:material-icons-core-android:1.7.8")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2") // Use the latest version

    // Serialization
    implementation("com.google.code.gson:gson:2.8.6")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
