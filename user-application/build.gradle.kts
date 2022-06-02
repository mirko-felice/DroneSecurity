plugins {
    id("dronesecurity-application")
}

val vertxVersion = "4.3.0"
val javaVersion = properties["java.version"].toString()

dependencies {
    implementation(project(":lib"))
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-openapi:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("io.vertx:vertx-mongo-client:$vertxVersion")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.apache.commons:commons-text:1.9")
}

val mainClassName by extra("$group.userapplication.controller.Launcher")
val mainModuleName by extra("$group.userapplication")

application {
    applicationDefaultJvmArgs = applicationDefaultJvmArgs.plus("--add-opens=javafx.graphics/javafx.scene=org.controlsfx.controls")
}

javafx {
    version = javaVersion
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

tasks {

    fun setup(task: org.gradle.jvm.tasks.Jar, osName: String) {
        task.group = "build"
        task.description = "Assembles a runnable fat jar archive containing all the needed stuff to be executed as standalone."
        task.archiveClassifier.set("${osName}-fat")
        task.archiveVersion.set("")
        task.from(sourceSets.main.get().output)
        task.dependsOn(configurations.compileClasspath)
        task.from(configurations.runtimeClasspath.get().map {
            if (it.name.contains("crt"))
                zipTree(it).matching { include { element ->
                    val osToRemove = when (osName) {
                        "macOs" -> "windows|linux".toRegex()
                        "Linux" -> "windows|osx".toRegex()
                        else -> "linux|osx".toRegex()
                    }
                    !element.file.path.contains(osToRemove)
                } }
            else
                zipTree(it)
        })
        task.manifest {
            val mainClass = project.extra["mainClassName"].toString()
            val lastName = mainClass.substring(mainClass.lastIndexOf("."))
            val withoutLastName = mainClass.replace(lastName, "")
            attributes["Main-Class"] = withoutLastName.replaceAfterLast(".", "Starter")
            attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
        }
        task.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    register<Jar>("LinuxFatJar") {
        setup(this, "Linux")
    }

    register<Jar>("WindowsFatJar") {
        setup(this, "Windows")
    }

    register<Jar>("macOSFatJar") {
        setup(this, "macOS")
    }
}
