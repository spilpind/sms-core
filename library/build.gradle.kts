import java.net.URI

group = "dk.spilpind"
version = "1.5.2"
val baseArtifactId = "sms-core"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)

    id("maven-publish")
}

kotlin {
    jvmToolchain(17)

    jvm()

    js(IR) {
        nodejs {
        }
    }

    iosArm64() // iOS device
    iosSimulatorArm64() // iOS simulator (on Apple silicon machine)
    iosX64() // iOS simulator (on Intel machine)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)
            }
        }
    }
}

publishing {
    publications {
        publications.withType<MavenPublication> {
            if (artifactId.startsWith(project.name, ignoreCase = true)) {
                artifactId = artifactId.replaceFirst(project.name, baseArtifactId)
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/spilpind/sms-core")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
