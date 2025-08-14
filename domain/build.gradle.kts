plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    jacoco
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.common)
}

android {
    namespace = "com.baghdad.domain"
}

dependencies {
    api(projects.entity)
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.bundles.test.core)
    implementation(libs.bundles.hilt)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}

kapt {
    correctErrorTypes = true
}