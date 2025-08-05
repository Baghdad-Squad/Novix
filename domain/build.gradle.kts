plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    jacoco
    alias(libs.plugins.kotlin.kapt)

}

android {
    namespace = "com.baghdad.domain"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(project(":entity"))
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.bundles.test.core)
    implementation(libs.bundles.hilt)
    implementation (libs.dagger)
    kapt (libs.dagger.compiler)
}

kapt{
    correctErrorTypes = true
}