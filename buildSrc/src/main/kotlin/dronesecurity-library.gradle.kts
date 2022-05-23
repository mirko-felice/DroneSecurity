import com.github.spotbugs.snom.Confidence

plugins {
    `java-library`
    checkstyle
    pmd
    jacoco
    id("de.jjohannes.extra-java-module-info")
    id("org.openjfx.javafxplugin")
    id("com.github.spotbugs")
}

val awsIotVersion = "1.8.5"
val javaVersion = properties["java.version"].toString()

val droneCertFileName = "Drone.cert.pem"
val privateKeyFileName = "Drone.private.key.pem"
val rootCAFileName = "root-CA.pem"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.12.0")
    spotbugsPlugins("com.mebigfatguy.sb-contrib:sb-contrib:7.4.7")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

pmd {
    ruleSetConfig = resources.text.fromFile(rootDir.path + properties["pmd.config"])
    ruleSets = emptyList()
}

spotbugs {
    reportLevel.set(Confidence.MIN_VALUE)
    excludeFilter.set(file(rootDir.path + properties["spotbugs.excludeFile"].toString()))
}

javafx {
    version = javaVersion
    modules("javafx.controls", "javafx.fxml")
}

tasks {

    register("createCertProperties") {
        doFirst {
            if (System.getenv("DRONE_CERT").isNotEmpty()) {
                val certFolderPath = mkdir("cert")
                certFolderPath.deleteOnExit()
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

                val projectProperties = file(projectDir.path + File.separator + "project.properties")
                projectProperties.deleteOnExit()
                projectProperties.appendText("certsFolderPath=" + certFolderPath.path + File.separator)
                projectProperties.appendText("\ncertificateFile=$droneCertFileName")
                projectProperties.appendText("\nprivateKeyFile=$privateKeyFileName")
                projectProperties.appendText("\ncertificateAuthorityFile=$rootCAFileName")
                projectProperties.appendText("\nendpoint=$endpoint")
                projectProperties.appendText("\nclientID=$username")

            }
        }
    }

    register("clearCerts") {
        doLast {
            val certFolderPath = file(projectDir.path + File.separator + "cert")

            file(certFolderPath.path + File.separator + droneCertFileName).delete()
            file(certFolderPath.path + File.separator + privateKeyFileName).delete()
            file(certFolderPath.path + File.separator + rootCAFileName).delete()

            file(projectDir.path + File.separator + "project.properties").delete()
            certFolderPath.delete()
        }
    }

    compileJava {
        doFirst {
            options.compilerArgs.add("-Xlint:deprecation")
            options.encoding = "UTF-8"
        }
    }

    test {
        dependsOn("createCertProperties")
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy(jacocoTestReport)
        finalizedBy("clearCerts")
    }

    jacocoTestReport {
        dependsOn(test)
        reports.xml.required.set(true)
    }

    spotbugsMain {
        reports {
            enabledReports.add(create("html"))
        }
    }

    spotbugsTest {
        reports {
            enabledReports.add(create("html"))
        }
    }

    javadoc {
        val options = options.windowTitle(project.name + " API")
        doFirst {
            val links = options.links
            configurations.runtimeClasspath.get().allDependencies
                .filter { it.name != "lib" }
                .map { it.group + "/" + it.name + "/" + it.version }
                .forEach { links?.add("https://javadoc.io/doc/$it") }
            links?.add("https://docs.oracle.com/en/java/javase/${javaVersion}/docs/api/")
        }
    }

}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
}
