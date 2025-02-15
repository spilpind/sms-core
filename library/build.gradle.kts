import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.net.URI

group = "dk.spilpind"
version = "1.6.0-dev"
val baseArtifactId = "sms-core"

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)

    id("maven-publish")
}

kotlin {
    setupTargets()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.datetime)
                implementation(libs.kotlin.serialization)
            }
        }
    }

    compilerOptions {
        val allWarningsAsErrorsArgument = "allWarningsAsErrors"
        if (project.hasProperty(allWarningsAsErrorsArgument)) {
            allWarningsAsErrors = (project.property(allWarningsAsErrorsArgument) == "true")
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


private fun KotlinMultiplatformExtension.setupTargets() {
    // We try to aim for as many targets as possible as we don't have any platform-specific code and just want to be as
    // available as possible. The targets here are inspired by this:
    // https://github.com/Kotlin/kotlinx.coroutines/blob/1.10.1/buildSrc/src/main/kotlin/kotlin-multiplatform-conventions.gradle.kts
    // (WASI was removed as it isn't supported by Kermit at the moment)

    jvm()
    jvmToolchain(17)

    iosArm64() // iOS device
    iosSimulatorArm64() // iOS simulator (on Apple Silicon machine)
    iosX64() // iOS simulator (on Intel machine)

    linuxX64()
    linuxArm64()
    mingwX64() // 64-bit Windows 7 and later using MinGW compatibility layer
    macosX64()
    macosArm64() // Apple macOS (on Apple Silicon machine)

    watchosArm32() // Apple watchOS on ARM32 platforms
    watchosArm64() // Apple watchOS on ARM64 platforms with ILP32
    watchosDeviceArm64() // Apple watchOS on ARM64 platforms
    watchosSimulatorArm64() // Apple watchOS simulator (on Apple Silicon machine)
    watchosX64() // Apple watchOS 64-bit simulator (on Intel machine)

    tvosArm64() // Apple tvOS on ARM64 platforms
    tvosSimulatorArm64() // Apple tvOS simulator (on Apple Silicon machine)
    tvosX64() // Apple tvOS simulator (on Intel machine)

    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()

    js {
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }
}
