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
    implementation("io.github.palexdev:materialfx:11.12.0")
    implementation("org.slf4j:slf4j-nop:1.7.32")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:win")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:linux")
    runtimeOnly("org.openjfx:javafx-fxml:$javafx.version:mac")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:win")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:linux")
    runtimeOnly("org.openjfx:javafx-controls:$javafx.version:mac")
}

val mainClassName by extra("$group.dronesecurity.userapplication.Launcher")
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