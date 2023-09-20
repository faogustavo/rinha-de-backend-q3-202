pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "rinha-de-backend-q3-2023"

include(":application")
include(":application:ktor")

include(":domain")
include(":domain:models")
include(":domain:core")

include(":datasource")

include(":datasource:database")
include(":datasource:database:db-core")
include(":datasource:database:exposed")

include(":datasource:cache:redis")