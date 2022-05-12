plugins {
    id("dronesecurity-library")
    application
}

application {
    afterEvaluate {
        mainModule.set(project.extra["mainModuleName"].toString())
        mainClass.set(project.extra["mainClassName"].toString())
    }
}

tasks {

    register<Jar>("fatJar") {
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

    distZip {
        enabled = false
    }

    distTar {
        enabled = false
    }

    startScripts {
        enabled = false
    }

}
