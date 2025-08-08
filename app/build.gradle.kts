plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.miniecommerceapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.miniecommerceapp"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // New dependencies for Ecommerce App:

    // Retrofit for networking
    implementation(libs.retrofit)
    // Gson Converter for JSON parsing
    implementation(libs.converter.gson)
    // OkHttp logging interceptor (good for debugging API calls)
    implementation(libs.logging.interceptor)

    // Kotlin Coroutines for asynchronous operations in ViewModelScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Coil for image loading (for product images)
    implementation(libs.coil.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose) // Use the latest stable version if different

    // For extended Material Icons (like Icons.Default.Remove)
    implementation(libs.androidx.material.icons.extended) // Use the latest stable version



}