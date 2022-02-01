plugins {
    id("configuration")
    id("org.openjfx.javafxplugin") version "0.0.11"
}

dependencies {
    implementation(libs.annotations)
    testImplementation(libs.jupiter)
    implementation("io.vertx:vertx-web:4.2.2")
    implementation("io.vertx:vertx-web-openapi:4.2.2")
    implementation("io.vertx:vertx-web-client:4.2.2")
    implementation("io.vertx:vertx-mongo-client:4.2.2")
    implementation("io.github.palexdev:materialfx:11.12.0")
    implementation("org.slf4j:slf4j-nop:1.7.32")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:1.5.4")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:win")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:linux")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:mac")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:win")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:linux")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:mac")
}

application {
    mainModule.set("it.unibo.dronesecurity.userapplication")
    mainClass.set("it.unibo.dronesecurity.userapplication.Launcher")
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.unibo.dronesecurity.userapplication.Launcher"
        attributes["Automatic-Module-Name"] = "it.unibo.dronesecurity.userapplication"
    }
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fat")
    from(sourceSets.main.get().output)
    dependsOn(configurations.compileClasspath)
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
    manifest {
        attributes["Main-Class"] = "it.unibo.dronesecurity.userapplication.Starter"
        attributes["Automatic-Module-Name"] = "it.unibo.dronesecurity.userapplication"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

extraJavaModuleInfo {
    module("vertx-web-4.2.2.jar", "io.vertx.web", "4.2.2") {
        exports("io.vertx.ext.web")
        requiresTransitive("io.vertx.core")
    }
    module("aws-crt-0.15.9.jar", "software.amazon.awssdk", "0.15.9") {
        exports("software.amazon.awssdk.crt")
        exports("software.amazon.awssdk.crt.mqtt")
        exports("software.amazon.awssdk.crt.io")
    }
    module("aws-iot-device-sdk-1.5.4.jar", "software.amazon.awssdk.iot", "1.5.4") {
        exports("software.amazon.awssdk.iot")
        requiresTransitive("software.amazon.awssdk")
    }
}