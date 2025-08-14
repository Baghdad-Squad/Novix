plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.common)
    jacoco
}

android {
    namespace = "com.baghdad.entity"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}