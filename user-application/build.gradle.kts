plugins {
    id("configuration")
    id("org.openjfx.javafxplugin") version "0.0.11"
}

val vertxVersion = "4.2.2"
val awsIotVersion = "1.5.4"
val awsCrtVersion = "0.15.15"

dependencies {
    implementation(libs.annotations)
    testImplementation(libs.jupiter)
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-openapi:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("io.vertx:vertx-mongo-client:$vertxVersion")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("org.slf4j:slf4j-nop:1.7.32")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:1.5.4")
}

val mainClassName by extra("$group.dronesecurity.userapplication.controller.Launcher")
val mainModuleName by extra("$group.dronesecurity.userapplication")

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

application {
    applicationDefaultJvmArgs = applicationDefaultJvmArgs.plus("--add-opens=javafx.graphics/javafx.scene=org.controlsfx.controls")
}

extraJavaModuleInfo {
    module("vertx-web-$vertxVersion.jar", "io.vertx.web", vertxVersion) {
        exports("io.vertx.ext.web")
        requiresTransitive("io.vertx.core")
    }
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