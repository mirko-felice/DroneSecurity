import java.util.Properties

plugins {
    application
    checkstyle
    pmd
    id("de.jjohannes.extra-java-module-info")
//    id("com.github.spotbugs")
}

group = "it.unibo"
val awsCrtVersion = "0.15.15"
val awsIotVersion = "1.5.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("software.amazon.awssdk.crt:aws-crt:$awsCrtVersion")
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

    getByName("run") {
        doFirst {
            setDebugMode(true)
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

    module("aws-crt-$awsCrtVersion.jar", "software.amazon.awssdk", awsCrtVersion) {
    exports("software.amazon.awssdk.crt")
    exports("software.amazon.awssdk.crt.mqtt")
    exports("software.amazon.awssdk.crt.io")
}
    module("aws-iot-device-sdk-$awsIotVersion.jar", "software.amazon.awssdk.iot", awsIotVersion) {
        exports("software.amazon.awssdk.iot")
        requiresTransitive("software.amazon.awssdk")
    }
}
