import java.util.Properties

plugins {
    application
    java
    checkstyle
    pmd
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

tasks.wrapper {
    gradleVersion = "7.3"
}

dependencies  {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("io.vertx:vertx-web:4.2.2")
    implementation("io.vertx:vertx-web-openapi:4.2.2")
    implementation("io.vertx:vertx-web-client:4.2.2")
    implementation("io.vertx:vertx-mongo-client:4.2.2")
    implementation("io.github.palexdev:materialfx:11.12.0")
    implementation("org.slf4j:slf4j-nop:1.7.32")
}

application {
    mainClass.set("controller.Launcher")
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

pmd {
    ruleSetConfig = resources.text.fromFile("config/pmd/pmd.xml")
    ruleSets = emptyList()
}

tasks.compileJava {
    doFirst {
        val properties = Properties()
        val file = resources.text.fromFile("src/main/resources/project.properties").asFile()
        properties.load(file.reader())
        properties.setProperty("isDebug", "true")
        properties.store(file.writer(), null)
    }
}

tasks.jar {
    doFirst {
        val properties = Properties()
        val file = resources.text.fromFile("src/main/resources/project.properties").asFile()
        properties.load(file.reader())
        properties.setProperty("isDebug", "false")
        properties.store(file.writer(), null)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("goAway") {
    doLast { println("Stay away from here!") }
}
