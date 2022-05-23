import com.github.spotbugs.snom.Confidence

plugins {
    `java-library`
    checkstyle
    pmd
    jacoco
    id("de.jjohannes.extra-java-module-info")
    id("org.openjfx.javafxplugin")
    id("com.github.spotbugs")
    `maven-publish`
    signing
}

val awsIotVersion = "1.8.5"
val javaVersion = properties["java.version"].toString()

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
    withSourcesJar()
    withJavadocJar()
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

publishing {
    publications {
        val projectVersion = project.version.toString()
        create<MavenPublication>("DroneSecurity") {
            from(components["java"])
            version = projectVersion
            pom {
                name.set("${groupId}:${artifactId}")
                description.set("System able to monitor the drone and manage the security thanks to some particular sensors.")
                url.set("https://github.com/mirko-felice/DroneSecurity")
                packaging = "jar"
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("mirko-felice")
                        name.set("Mirko Felice")
                        email.set("mirko.felice@studio.unibo.it")
                    }
                    developer {
                        id.set("maxim-derevyanchenko")
                        name.set("Maxim Derevyanchenko")
                        email.set("maxim.derevyanchenko@studio.unibo.it")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mirko-felice/DroneSecurity.git")
                    developerConnection.set("scm:git:ssh://github.com/mirko-felice/DroneSecurity.git")
                    url.set("http://github.com/mirko-felice/DroneSecurity")
                }
                repositories {
                    maven {

                    }
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["DroneSecurity"])
}

tasks {

    withType<PublishToMavenRepository>().configureEach {
        onlyIf { !project.version.toString().startsWith("0") }
    }

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
        options.addBooleanOption("html5", true)
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
