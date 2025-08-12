plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.1.4")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
}

gradlePlugin {
    plugins {
        register("commonPlugin") {
            id = "com.baghdad.build_logic.common"
            implementationClass = "com.baghdad.build_logic.CommonPlugin"
        }
    }
}
repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}