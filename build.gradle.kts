// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    jacoco
}


/**
 * When you need to exclude specific packages or classes from the Jacoco report,
 * you can add them to this list.
 * */
val excludedPackagesOrClasses = listOf(
    "com.example.domain.util",
)

val execPaths = listOf(
    "**/build/jacoco/testDebugUnitTest.exec",
    "**/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
)

val excludedPatterns = excludedPackagesOrClasses.flatMap { path ->
    val patternBase = path.replace('.', '/')
    listOf(
        "**/$patternBase/**",
        "**/$patternBase.class",
        "**/$patternBase\$*.class"
    )
}

val defaultExcludes = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*\$WhenMappings.*",
    "**/*\$serializer.*",
    "**/*\$Companion.*"
)

val sourceDirs = subprojects.flatMap { project ->
    listOf(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )
}.filter { file(it).exists() }

val allExcludes = defaultExcludes + excludedPatterns

val classDirs = subprojects.flatMap { project ->
    listOf(
        fileTree("${project.buildDir}/tmp/kotlin-classes/debug"),
        fileTree("${project.buildDir}/intermediates/javac/debug/classes"),
        fileTree("${project.buildDir}/classes/kotlin/debug"),
        fileTree("${project.buildDir}/intermediates/javac/debug"),
        fileTree("${project.buildDir}/classes/java/debug")
    ).filter { it.dir.exists() }
}.map { dirTree ->
    dirTree.matching {
        exclude(allExcludes)
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(subprojects.map { "${it.path}:testDebugUnitTest" })
    executionData.setFrom(fileTree(rootDir).matching { include(execPaths) })
    sourceDirectories.setFrom(sourceDirs)
    classDirectories.setFrom(classDirs)
    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("${buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"))
        html.outputLocation.set(file("${buildDir}/reports/jacoco/jacocoTestReport/html"))
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
