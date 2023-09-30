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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":datasource:database:exposed"))
                implementation(project(":datasource:database:raw-jdbc"))

                implementation(project(":datasource:cache:redis"))
                implementation(project(":datasource:cache:no-op"))
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