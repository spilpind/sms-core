plugins {
    kotlin("multiplatform") version "1.5.10"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js(LEGACY) {
        nodejs {
        }
    }
}
