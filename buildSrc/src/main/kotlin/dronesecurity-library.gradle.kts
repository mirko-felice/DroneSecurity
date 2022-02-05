plugins {
    `java-library`
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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
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

tasks {

    compileJava {
        doFirst {
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }

    test {
        useJUnitPlatform()
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
