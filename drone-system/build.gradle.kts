plugins {
    id("dronesecurity-application")
}

val apacheExecVersion = "1.3"
val apacheLangVersion = "3.12.0"

dependencies  {
    implementation(project(":lib"))
    implementation("org.apache.commons:commons-exec:$apacheExecVersion")
    implementation("org.apache.commons:commons-lang3:$apacheLangVersion")
}

val mainClassName by extra("$group.dronesystem.drone.Activation")
val mainModuleName by extra("$group.dronesystem")

extraJavaModuleInfo {
    module("commons-exec-$apacheExecVersion.jar", "org.apache.commons.exec", apacheExecVersion) {
        exports("org.apache.commons.exec")
    }

    module("commons-lang3-$apacheLangVersion.jar", "org.apache.commons.lang3", apacheLangVersion) {
        exports("org.apache.commons.lang3")
    }
}
