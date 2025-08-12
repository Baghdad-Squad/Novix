plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("com.baghdad.build_logic.common")
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
    testImplementation("androidx.paging:paging-testing:3.3.0")
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

}

kapt {
    correctErrorTypes = true
}

