import java.util.Properties

plugins {
    application
    checkstyle
    pmd
    id("de.jjohannes.extra-java-module-info")
//    id("com.github.spotbugs")
}

group = "it.unibo"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

pmd {
    ruleSetConfig = resources.text.fromFile(rootDir.path + File.separator + "config" + File.separator + "pmd" + File.separator + "pmd.xml")
    ruleSets = emptyList()
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

    compileJava {
        doFirst {
            setDebugMode(true)
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }

    jar {
        doFirst {
            setDebugMode(false)
            manifest {
                attributes["Main-Class"] = project.extra["mainClassName"]
                attributes["Automatic-Module-Name"] = project.extra["mainModuleName"]
            }
        }
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

    test {
        useJUnitPlatform()
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

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
}