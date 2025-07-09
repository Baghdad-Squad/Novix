plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}


/**
 * When you need to exclude specific packages or classes from the Jacoco report,
 * you can add them to this list.
 * */
val excludedPackagesOrClasses = listOf(
    "com.baghdad.entity",
    "com.baghdad.design_system",
    "com.baghdad.islamic_image_loader"
)

val excludedPatterns = excludedPackagesOrClasses.flatMap { path ->
    val patternBase = path.replace('.', '/')
    listOf(
        "**/$patternBase/**",
        "**/$patternBase.class",
        "**/$patternBase\$*.class"
    )
}


val defaultExcludedPackagesOrClasses = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*_Impl*.*",
    "**/ComposableSingletons*.*",
    "**/*Kt.class",
    "**/MainActivity.class",
    "**/di/**",
    "**/ui/theme/**",
)

val allExcludes = defaultExcludedPackagesOrClasses + excludedPatterns

val classDirs = subprojects.mapNotNull { subproject ->
    val path = "${subproject.buildDir}/tmp/kotlin-classes/debug"
    val dir = file(path)
    if (dir.exists()) {
        fileTree(dir) {
            exclude(allExcludes)
        }
    } else null
}

val execPaths = listOf(
    "**/build/jacoco/testDebugUnitTest.exec",
    "**/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
)


val sourceDirs = subprojects.flatMap {
    listOf(
        "${it.projectDir}/src/main/java",
        "${it.projectDir}/src/main/kotlin"
    )
}.filter { file(it).exists() }

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(subprojects.map { it.path + ":testDebugUnitTest" })
    executionData.setFrom(fileTree(rootDir).matching { include(execPaths) })

    classDirectories.setFrom(classDirs)
    sourceDirectories.setFrom(files(sourceDirs))

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(file("app/build/reports/jacoco/jacocoTestReport/html"))
        xml.outputLocation.set(file("app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"))
    }
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")
    executionData.setFrom(fileTree(rootDir).matching { include(execPaths) })
    classDirectories.setFrom(classDirs)

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}