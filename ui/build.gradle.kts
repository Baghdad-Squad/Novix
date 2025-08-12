plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    id("com.baghdad.build_logic.common")
}

android {
    namespace = "com.baghdad.ui"
}

dependencies {
    api(projects.islamicImageLoader)
    implementation(projects.designSystem)
    implementation(projects.viewmodel)
    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.datetime)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.bundles.test.core)
    androidTestImplementation(libs.bundles.test.core)
    androidTestImplementation(libs.bundles.test.ui)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.paging.compose)

}

kapt {
    correctErrorTypes = true
}