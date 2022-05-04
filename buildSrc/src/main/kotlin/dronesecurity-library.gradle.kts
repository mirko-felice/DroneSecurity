import com.github.spotbugs.snom.Confidence
import java.util.Properties

plugins {
    `java-library`
    checkstyle
    pmd
    id("de.jjohannes.extra-java-module-info")
    id("org.openjfx.javafxplugin")
    id("com.github.spotbugs")
    id("org.sonarqube")
}

val awsCrtVersion = "0.16.7"
val awsIotVersion = "1.5.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("software.amazon.awssdk.crt:aws-crt:$awsCrtVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.10.0")
    spotbugsPlugins("com.mebigfatguy.sb-contrib:sb-contrib:7.4.7")
    implementation("ch.qos.logback:logback-classic:1.2.10")
}

sonarqube.properties {
    val sonarProperties = Properties()
    file("../sonar.properties").inputStream().use { sonarProperties.load(it) }
    property("sonar.login", sonarProperties.getProperty("token"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
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
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

tasks {

    compileJava {
        doFirst {
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }

    test {
        useJUnitPlatform()
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
            links?.add("https://docs.oracle.com/en/java/javase/11/docs/api/")
//            classpath.files.addAll(files(project(":lib").sourceSets.main.get().allJava))
        }
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
