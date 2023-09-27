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
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

