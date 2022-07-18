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
