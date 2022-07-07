import com.github.spotbugs.snom.Confidence
import java.util.regex.Pattern

plugins {
    `java-library`
    checkstyle
    pmd
    jacoco
    id("org.openjfx.javafxplugin")
    id("com.github.spotbugs")
    `maven-publish`
    signing
}

val awsIotVersion = "1.9.3"
val javaVersion = properties["java.version"].toString()

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.awaitility:awaitility:4.2.0")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
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

checkstyle {
    toolVersion = "10.3.1"
}

pmd {
    ruleSetConfig = resources.text.fromFile(rootDir.path + properties["pmd.config"])
    ruleSets = emptyList()
}

spotbugs {
    reportLevel.set(Confidence.MIN_VALUE)
    excludeFilter.set(file(rootDir.path + properties["spotbugs.excludeFile"].toString()))
}

publishing {
    publications {
        val baseVersion = project.version.toString()
        val projectVersion =
            if (baseVersion.contains("-"))
                baseVersion.substringBefore("-") + "-SNAPSHOT"
            else
                baseVersion
        create<MavenPublication>("DroneSecurity") {
            from(components["java"])
            version = projectVersion
            pom {
                groupId = "io.github.mirko-felice.dronesecurity"
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
                    url.set("https://github.com/mirko-felice/DroneSecurity")
                }
            }
        }
        repositories {
            maven {
                val releasesUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (projectVersion.endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl)
                credentials {
                    val mavenUsername: String? by project
                    username = mavenUsername
                    val mavenPassword: String? by project
                    password = mavenPassword
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
        val baseVersion = project.version.toString()
        val projectVersion =
            if (baseVersion.contains("-"))
                baseVersion.substringBefore("-") + "-SNAPSHOT"
            else
                baseVersion
        onlyIf { Pattern.matches("(([0-9])+(\\.?([0-9]))*)+(-SNAPSHOT)?", projectVersion) }
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
            links?.add("https://javadoc.io/doc/io.github.mirko-felice.dronesecurity/lib/latest")
        }
    }

}
