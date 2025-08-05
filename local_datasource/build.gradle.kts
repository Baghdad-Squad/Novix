import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.google.protobuf") version "0.9.4"
    alias(libs.plugins.kotlin.kapt)
    jacoco
}

android {
    namespace = "com.baghdad.local_datasource"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(project(":repository"))
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.room)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.runner)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.bundles.test.core)
    androidTestImplementation(libs.bundles.test.core)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)
}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.7"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") { // id is imported above
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}