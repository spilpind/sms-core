import java.net.URI

group = "dk.spilpind"
version = "1.5.1"
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

    iosX64()
    iosArm64()

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
            artifactId = if (name == "kotlinMultiplatform") {
                baseArtifactId
            } else {
                "$baseArtifactId-$name"
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
