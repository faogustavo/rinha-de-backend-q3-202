plugins {
    kotlin("jvm") version "1.9.0"
    application
    kotlin("plugin.serialization") version "1.9.0"
    id("io.ktor.plugin") version libs.versions.ktor
}

group = "dev.valvassori"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.datetime)
    implementation(libs.postgres.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.jedis)

    implementation(libs.ktor.core)
    implementation(libs.ktor.statusPage)

    implementation(libs.ktor.engine.cio)
    implementation(libs.ktor.engine.netty)
    implementation(libs.ktor.engine.jetty)
    implementation(libs.ktor.engine.tomcat)

    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.call.logging)
    implementation(libs.ktor.serialization.json)

    implementation(libs.logback.classic)

    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.helpers.test)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
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