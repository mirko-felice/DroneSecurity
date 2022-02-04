plugins {
    id("dronesecurity-application")
}

val apacheCommonsVersion = "1.3"

dependencies  {
    implementation(project(":lib"))
    implementation("org.apache.commons:commons-exec:$apacheCommonsVersion")
}

val mainClassName by extra("$group.dronesecurity.dronesystem.Activation")
val mainModuleName by extra("$group.dronesecurity.dronesystem")

extraJavaModuleInfo {
    module("commons-exec-$apacheCommonsVersion.jar", "org.apache.commons.exec", apacheCommonsVersion) {
        exports("org.apache.commons.exec")
    }
}
