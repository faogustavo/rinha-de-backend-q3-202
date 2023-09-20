plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "dev.valvassori"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets.all {
        languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization)
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
