rootProject.name = "sms-core"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":library")

// This name will be used as module name by the generated iOS libraries (and for instance also as
// name for the generated klibs) and will therefore be used as prefix for the Swift name of the
// classes available in the swift-projects (when this library is included in a shared KMP module).
// For instance can the Team interface be referenced as SmsCoreTeam.
// Conclusion: Don't change this unless we want to change that prefix!
project(":library").name = "SmsCore"
