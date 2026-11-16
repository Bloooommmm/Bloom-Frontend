plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "id.ac.binus.bloomapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "id.ac.binus.bloomapp"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BLOOM_API_BASE_URL", "\"https://bloom-be.yanzz.dev\"")
    }

    buildTypes {
        release {
            val ciKeystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
            val ciStorePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            val ciKeyAlias = System.getenv("ANDROID_KEY_ALIAS")
            val ciKeyPassword = System.getenv("ANDROID_KEY_PASSWORD")

            if (!ciKeystorePath.isNullOrBlank() && !ciStorePassword.isNullOrBlank() && !ciKeyAlias.isNullOrBlank() && !ciKeyPassword.isNullOrBlank()) {
                signingConfig = signingConfigs.create("ciRelease") {
                    storeFile = file(ciKeystorePath)
                    storePassword = ciStorePassword
                    keyAlias = ciKeyAlias
                    keyPassword = ciKeyPassword
                }
            }

            optimization {
                enable = false
            }
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
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.compose)
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
