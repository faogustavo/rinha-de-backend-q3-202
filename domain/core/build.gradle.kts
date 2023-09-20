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
                api(project(":domain:models"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
