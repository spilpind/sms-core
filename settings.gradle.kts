rootProject.name = "sms-core"

pluginManagement {
    plugins {
        val kotlinVersion = "2.0.0"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}
