rootProject.name = "DroneSecurity"
include("drone-system", "user-application")

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            alias("annotations").to("org.jetbrains:annotations:23.0.0")
            alias("junit").to("org.junit.jupiter:junit-jupiter:5.8.2")
        }
    }
}
