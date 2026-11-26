pluginManagement {
    repositories {
        google()
        // Google-hosted mirror of Maven Central reduces direct Central rate-limit exposure.
        maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Keep Maven Central as a fallback for artifacts not yet mirrored.
        maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
        mavenCentral()
    }
}
rootProject.name = "SarangshoPlus"
include(":app")
