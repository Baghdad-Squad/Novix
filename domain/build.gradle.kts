import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    api(project(":entity"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.dagger)
    testImplementation(libs.bundles.test.core)
    kapt (libs.dagger.compiler)
}

kapt{
    correctErrorTypes = true
}