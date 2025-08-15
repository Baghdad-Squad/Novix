plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.common)
    jacoco
}

android {
    namespace = "com.baghdad.viewmodel"
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

dependencies {
    implementation(projects.domain)
    implementation(libs.bundles.coroutines)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.common.android)
    testImplementation(libs.bundles.test.core)
    testImplementation(libs.androidx.paging.testing)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}

