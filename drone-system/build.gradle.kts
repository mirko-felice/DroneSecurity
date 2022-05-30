plugins {
    id("dronesecurity-application")
    id("de.jjohannes.extra-java-module-info")
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

    failOnMissingModuleInfo.set(false)
}

tasks.register<Jar>("DroneFatJar") {
    group = "build"
    description = "Assembles a runnable fat jar archive containing all the needed stuff to be executed as standalone."
    archiveClassifier.set("fat")
    archiveVersion.set("")
    from(sourceSets.main.get().output)
    dependsOn(configurations.compileClasspath)
    from(configurations.runtimeClasspath.get().map {
        if (it.name.contains("crt"))
            zipTree(it).matching { include { element -> !element.path.contains("windows") && !element.path.contains("osx") } }
        else
            zipTree(it)
    })
    manifest {
        attributes["Main-Class"] = project.extra["mainClassName"]
        attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
