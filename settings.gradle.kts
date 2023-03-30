rootProject.name = "sms-core"

pluginManagement {
    plugins {
        val kotlinVersion = "1.8.10"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

