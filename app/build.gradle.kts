plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    jacoco
}

android {
    namespace = "com.baghdad.novix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.baghdad.novix"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":design_system"))
    implementation(project(":viewmodel"))
    implementation(project(":repository"))
    implementation(project(":local_datasource"))
    implementation(project(":remote_datasource"))
    implementation(project(":domain"))
    implementation(project(":entity"))
    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.koin)
    implementation(libs.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}

/**
 * to install git hooks use this command line
 * ./gradlew installGitHooks
 **/

tasks.register("installGitHooks") {
    doLast {
        fun installHook(name: String) {
            val source = rootProject.file("scripts/hooks/$name")
            val target = rootProject.file(".git/hooks/$name")

            if (!target.exists() || target.readText() != source.readText()) {
                target.writeText(source.readText())
                target.setExecutable(true)
                source.setReadable(true)
                source.setWritable(false)
                println("✅ $name hook installed and made executable.")
            } else {
                println("✅ $name hook is already up to date.")
            }
        }

        installHook("commit-msg")
        installHook("pre-push")
    }
}

gradle.projectsEvaluated {
    tasks["build"].dependsOn("installGitHooks")
}
