rootProject.name = "KotlinProject"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // For Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") // For WASM
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") // For Ktor WASM

    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // For Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") // For WASM
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") // For Ktor WASM
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

include(":composeApp")