pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Novix"
include(":app")
include(":ui")
include(":design_system")
include(":viewmodel")
include(":domain")
include(":entity")
include(":repository")
include(":local_datasource")
include(":remote_datasource")
