rootProject.name = "sms-core"

pluginManagement {
    plugins {
        val kotlinVersion = "1.7.0"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

