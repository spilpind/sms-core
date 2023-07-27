rootProject.name = "sms-core"

pluginManagement {
    plugins {
        val kotlinVersion = "1.9.0"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}
