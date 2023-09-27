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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain:core"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(libs.hikaricp)
                api(libs.postgres.jdbc)
                api(libs.mysql.jdbc)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

