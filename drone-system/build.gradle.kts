import java.util.Properties

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

val mainClassName by extra("$group.dronesystem.drone.Starter")
val mainModuleName by extra("$group.dronesystem")
val performanceMainClass by extra("$group.dronesystem.performance.PerformanceEvaluator")

extraJavaModuleInfo {
    module("commons-exec-$apacheExecVersion.jar", "org.apache.commons.exec", apacheExecVersion) {
        exports("org.apache.commons.exec")
    }

    failOnMissingModuleInfo.set(false)
}

tasks {
    val propertiesFile = file(
            (if (System.getenv("CI") == true.toString())
                projectDir
            else
                rootDir)
                    .path + File.separator + "project.properties")

    register("createCerts") {
        doFirst {
            if (System.getenv("CI") == true.toString()) {
                val droneCertFileName = "Drone.cert.pem"
                val privateKeyFileName = "Drone.private.key.pem"
                val rootCAFileName = "root-CA.pem"
                val certFolderPath = mkdir("certs")
                val droneCert = file(certFolderPath.path + File.separator + droneCertFileName)
                val privateKey = file(certFolderPath.path + File.separator + privateKeyFileName)
                val rootCA = file(certFolderPath.path + File.separator + rootCAFileName)
                val username = if (certFolderPath.path.contains(File.separator + "user-application" + File.separator)) {
                    "DroneSecurity: User"
                } else {
                    "DroneSecurity: Drone"
                }

                droneCert.appendText(System.getenv("DRONE_CERT"))
                privateKey.appendText(System.getenv("PRIVATE_KEY"))
                rootCA.appendText(System.getenv("ROOT_CA"))
                val endpoint = System.getenv("ENDPOINT")

                val projectProperties = Properties()
                projectProperties.setProperty("certsFolderPath", certFolderPath.path + File.separator)
                projectProperties.setProperty("certificateFile", droneCertFileName)
                projectProperties.setProperty("privateKeyFile", privateKeyFileName)
                projectProperties.setProperty("certificateAuthorityFile", rootCAFileName)
                projectProperties.setProperty("endpoint", endpoint)
                projectProperties.setProperty("clientID", username)
                val outputStream = propertiesFile.outputStream()
                projectProperties.store(outputStream, "Connection settings")
                outputStream.close()
            } else {
                copy {
                    from(rootDir)
                    include("project.properties")
                    into(projectDir.path)
                }
            }
        }
    }

    register("clearCerts") {
        doLast {
            if (System.getenv("CI") == true.toString()) {
                val certFolderPath = file(projectDir.path + File.separator + "certs")
                certFolderPath.deleteRecursively()
            }
            propertiesFile.delete()
        }
    }

    register<Jar>("DroneFatJar") {
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

    register<Jar>("PerformanceFatJar") {
        archiveClassifier.set("performance-fat")
        archiveVersion.set("")

        dependsOn(processResources)
        doFirst {
            copy {
                val sep = File.separator
                val performanceScriptsDirPath = sourceSets.main.get().output.resourcesDir?.path + sep + mainModuleName.replace(".", sep) + sep + "performance" + sep + "drone"
                from(performanceScriptsDirPath)
                into(performanceScriptsDirPath + sep + ".." + sep + ".." + sep + "drone")
            }
        }

        from(sourceSets.main.get().output)
        dependsOn(configurations.compileClasspath)
        from(configurations.runtimeClasspath.get().map {
            if (it.name.contains("crt"))
                zipTree(it).matching { include { element -> !element.path.contains("windows") && !element.path.contains("osx") } }
            else
                zipTree(it)
        })
        manifest {
            attributes["Main-Class"] = project.extra["performanceMainClass"]
            attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    test {
        dependsOn("createCerts")
        finalizedBy("clearCerts")
    }
}
