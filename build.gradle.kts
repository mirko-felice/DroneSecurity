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
    implementation("io.vertx:vertx-web:4.2.1")
    implementation("io.vertx:vertx-web-openapi:4.2.1")
    implementation("io.github.palexdev:materialfx:11.12.0")
}

application {
    mainClass.set("controller.Controller")
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("goAway") {
    doLast { println("Stay away from here!") }
}
