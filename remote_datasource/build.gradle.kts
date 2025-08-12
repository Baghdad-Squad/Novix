plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    id("com.baghdad.build_logic.common")
    jacoco
}

android {
    namespace = "com.baghdad.remote_datasource"
}

dependencies {
    implementation(projects.repository)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.bundles.test.core)
    testImplementation(libs.ktor.client.mock)
    implementation(libs.bundles.retrofit)
    testImplementation(libs.mockwebserver)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}