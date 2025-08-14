plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.common)
}

android {
    namespace = "com.baghdad.design_system"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.androidx.core)
    implementation(libs.androidx.foundation.v167)
    implementation(libs.bundles.material)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.readmore.material3)
    implementation(libs.bundles.coil)
}
