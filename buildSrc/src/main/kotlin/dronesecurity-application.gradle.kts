import java.util.Properties

plugins {
    id("dronesecurity-library")
    application
}

fun setDebugMode(value: Boolean) {
    val properties = Properties()
    val file = resources.text.fromFile(rootDir.path + File.separator + "project.properties").asFile()
    properties.load(file.reader())
    properties.setProperty("isDebug", value.toString())
    properties.store(file.writer(), null)
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
        doFirst {
            setDebugMode(false)
        }
        archiveClassifier.set("fat")
        from(sourceSets.main.get().output)
        dependsOn(configurations.compileClasspath)
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
        manifest {
            val mainClass = project.extra["mainClassName"]
            attributes["Main-Class"] = if (project.name == "user-application") mainClass.toString().replace("controller.Launcher", "Starter") else mainClass
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
