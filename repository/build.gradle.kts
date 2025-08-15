plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.common)
    jacoco
    kotlin("kapt")
}

android {
    namespace = "com.baghdad.repository"
}

dependencies {
    implementation(projects.domain)
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.bundles.test.core)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}