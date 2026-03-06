pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "system-properties-plugin"
plugins {
    id("com.gradle.develocity") version "4.3.1"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2.4.0"
}

val isCI = providers.environmentVariable("CI").isPresent

develocity {
    server = "https://ge.solutions-team.gradle.com"
    buildScan {
        uploadInBackground = !isCI
        publishing.onlyIf { it.isAuthenticated }
        obfuscation {
            ipAddresses { addresses -> addresses.map { "0.0.0.0" } }
        }
    }
}
