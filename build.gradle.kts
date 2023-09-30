plugins {
    kotlin("multiplatform") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("io.ktor.plugin") version libs.versions.ktor apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
    group = "dev.valvassori"
    version = "1.0"
}
