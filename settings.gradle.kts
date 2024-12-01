pluginManagement {
    // remove when you don't have paperweight checked out locally
    // includeBuild("..")
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "MiniFork"

include("MiniFork-api", "MiniFork-server")
