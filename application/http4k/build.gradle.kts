import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("multiplatform")
    id("com.github.johnrengelman.shadow")
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
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:core"))
                implementation(libs.kotlinx.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":application:app-core"))
                implementation(libs.http4k.core)
                implementation(libs.http4k.json.kotlinx)
                implementation(libs.http4k.server.netty)
            }
        }
    }
}

application {
    mainClass.set("dev.valvassori.rinha.MainKt")
}

tasks.named<ShadowJar>("shadowJar") {
    manifest.attributes["Main-Class"] = "dev.valvassori.rinha.MainKt"
    archiveFileName.set("app.jar")
    mergeServiceFiles()
}