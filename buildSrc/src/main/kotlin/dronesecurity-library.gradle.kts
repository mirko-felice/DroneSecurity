import com.github.spotbugs.snom.Confidence
import com.lordcodes.turtle.shellRun

plugins {
    `java-library`
    checkstyle
    pmd
    jacoco
    id("de.jjohannes.extra-java-module-info")
    id("org.openjfx.javafxplugin")
    id("com.github.spotbugs")
    id("org.sonarqube")
}

val awsIotVersion = "1.8.4"
val javaVersion = properties["java.version"].toString()

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.10.0")
    spotbugsPlugins("com.mebigfatguy.sb-contrib:sb-contrib:7.4.7")
    implementation("ch.qos.logback:logback-classic:1.2.10")
}

project.version = shellRun {
    git.gitCommand(listOf("describe", "--tags"))
}

sonarqube.properties {
    property("sonar.projectKey", "DroneSecurity")
    property("sonar.organization", "mirko-felice")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.sources", sourceSets.main.get().allJava.srcDirs)
    property("sonar.tests", sourceSets.test.get().allJava.srcDirs)
    property("sonar.python.version", 3.7)
    property("sonar.projectName", project.name)
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

    val sonar = tasks.getByName("sonarqube")
    sonar.dependsOn(test)

    compileJava {
        doFirst {
            options.compilerArgs.add("-Xlint:deprecation")
            options.encoding = "UTF-8"
        }
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy(jacocoTestReport)
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
