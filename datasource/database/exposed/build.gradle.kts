plugins {
    kotlin("multiplatform")
}

group = "dev.valvassori"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    jvmToolchain(17)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:core"))
                implementation(project(":datasource:database:db-core"))
                implementation(libs.kotlinx.serialization)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.exposed.core)
                implementation(libs.exposed.jdbc)
                implementation(libs.exposed.datetime)
            }
        }
    }
}

