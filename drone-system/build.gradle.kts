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

tasks.register<Jar>("DroneFatJar") {
    group = "build"
    description = "Assembles a runnable fat jar archive containing all the needed stuff to be executed as standalone."
    archiveClassifier.set("fat")
    archiveVersion.set("")
    from(sourceSets.main.get().output)
    dependsOn(configurations.compileClasspath)
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
    manifest {
        val mainClass = project.extra["mainClassName"]
        val lastName = mainClass.toString().substring(mainClass.toString().lastIndexOf("."))
        val withoutLastName = mainClass.toString().replace(lastName, "")
        attributes["Main-Class"] = withoutLastName.replaceAfterLast(".", "Starter")
        attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
