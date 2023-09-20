plugins {
    application
    kotlin("multiplatform")
    id("io.ktor.plugin")
}

group = "dev.valvassori"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm { withJava() }
    jvmToolchain(17)

    sourceSets.all {
        languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":domain:core"))
                implementation(project(":datasource:database:exposed"))
                implementation(project(":datasource:cache:redis"))

                implementation(libs.ktor.core)
                implementation(libs.ktor.statusPage)
                implementation(libs.ktor.server.contentNegotiation)
                implementation(libs.ktor.call.logging)
                implementation(libs.ktor.serialization.json)

                implementation(libs.ktor.engine.cio)
                implementation(libs.ktor.engine.netty)
                implementation(libs.ktor.engine.jetty)
                implementation(libs.ktor.engine.tomcat)

                implementation(libs.logback.classic)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.helpers.test)
            }
        }
    }
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

application {
    mainClass.set("dev.valvassori.rinha.MainKt")
}