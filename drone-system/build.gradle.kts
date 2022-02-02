plugins {
    id("configuration")
}

val apacheCommonsVersion = "1.3"

dependencies  {
    implementation("org.apache.commons:commons-exec:$apacheCommonsVersion")
    implementation("com.google.code.gson:gson:2.8.6")
}

val mainClassName by extra("$group.dronesecurity.dronesystem.Activation")
val mainModuleName by extra("$group.dronesecurity.dronesystem")

extraJavaModuleInfo {
    module("commons-exec-$apacheCommonsVersion.jar", "org.apache.commons.exec", apacheCommonsVersion) {
        exports("org.apache.commons.exec")
    }
}
