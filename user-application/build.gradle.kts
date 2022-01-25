plugins {
    id("configuration")
    id("org.openjfx.javafxplugin") version "0.0.11"
}

val vertxVersion = "4.2.2"

dependencies {
    implementation(libs.annotations)
    testImplementation(libs.jupiter)
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-openapi:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("io.vertx:vertx-mongo-client:$vertxVersion")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("org.slf4j:slf4j-nop:1.7.32")
}

val mainClassName by extra("$group.dronesecurity.userapplication.controller.Launcher")
val mainModuleName by extra("$group.dronesecurity.userapplication")

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

extraJavaModuleInfo {
    module("vertx-web-$vertxVersion.jar", "io.vertx.web", vertxVersion) {
        exports("io.vertx.ext.web")
        requiresTransitive("io.vertx.core")
    }
}