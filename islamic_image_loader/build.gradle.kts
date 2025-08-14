plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.common)
}

android {
    namespace = "com.baghdad.islamic_image_loader"

    buildFeatures {
        compose = true
        mlModelBinding = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.core)
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.tensorflow)
    implementation(libs.tensorflow.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
}
