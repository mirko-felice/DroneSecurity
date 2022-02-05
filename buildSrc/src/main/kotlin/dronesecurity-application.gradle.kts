import java.util.Properties

plugins {
    id("dronesecurity-library")
    application
}

fun setProjectProperty(property: String, value: String) {
    val properties = Properties()
    val file = resources.text.fromFile(rootDir.path + File.separator + "project.properties").asFile()
    if (!file.exists())
        file.createNewFile()
    properties.load(file.reader())
    properties.setProperty(property, value)
    properties.store(file.writer(), null)
}

fun setDebugMode(value: Boolean) {
    setProjectProperty("isDebug", value.toString())
}

fun setClientID(clientID: String) {
    setProjectProperty("clientID", clientID)
}

application {
    afterEvaluate {
        mainModule.set(project.extra["mainModuleName"].toString())
        mainClass.set(project.extra["mainClassName"].toString())
    }
}

tasks {

    jar {
        doFirst {
            setDebugMode(false)
            if (project.name == "user-application")
                setClientID("User")
            else
                setClientID("Drone")
            manifest {
                attributes["Main-Class"] = project.extra["mainClassName"]
                attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
            }
        }
    }

    val run: JavaExec by tasks
    run.doFirst {
        setDebugMode(true)
    }

    register<Jar>("fatJar") {
        archiveClassifier.set("fat")
        from(sourceSets.main.get().output)
        dependsOn(configurations.compileClasspath)
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
        manifest {
            val mainClass = project.extra["mainClassName"]
            attributes["Main-Class"] = if (project.name == "user-application") mainClass.toString().replace("Launcher", "Starter") else mainClass
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
