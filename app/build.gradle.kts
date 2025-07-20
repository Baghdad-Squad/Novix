import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    jacoco
}

val formattedDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm"))

android {
    namespace = "com.baghdad.novix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.baghdad.novix"
        minSdk = 24
        targetSdk = 35
        versionName = "1.0-($formattedDate)"
        versionCode = 1

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("app/secrets.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("API_KEY") ?: ""
        val baseUrl = properties.getProperty("BASE_URL") ?: ""

        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = apiKey
        )

        buildConfigField(
            type = "String",
            name = "BASE_URL",
            value = baseUrl
        )
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")

            val localProps = Properties()
            val localPropsFile = rootProject.file("local.properties")
            if (localPropsFile.exists()) {
                localProps.load(localPropsFile.inputStream())
            }

            storePassword = localProps.getProperty("RELEASE_KEYSTORE_PASSWORD")
                ?: System.getenv("RELEASE_KEYSTORE_PASSWORD") ?: ""
            keyAlias = localProps.getProperty("RELEASE_KEY_ALIAS")
                ?: System.getenv("RELEASE_KEY_ALIAS") ?: ""
            keyPassword = localProps.getProperty("RELEASE_KEY_PASSWORD")
                ?: System.getenv("RELEASE_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
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
    firebaseCrashlytics {
        mappingFileUploadEnabled = true
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
        buildConfig = true
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
    implementation(project(":islamic_image_loader"))
    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.ui)
    implementation(libs.bundles.koin)
    implementation(libs.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.ktor.client)
    testImplementation(libs.bundles.test.core)
    implementation(libs.bundles.room)
    implementation(libs.bundles.retrofit)
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
