plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.baghdad.build_logic.common")
    jacoco
}

android {
    namespace = "com.baghdad.entity"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}