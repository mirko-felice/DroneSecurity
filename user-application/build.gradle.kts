plugins {
    id("dronesecurity-application")
}

val vertxVersion = "4.3.0"

dependencies {
    implementation(project(":lib"))
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-openapi:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("io.vertx:vertx-mongo-client:$vertxVersion")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("commons-codec:commons-codec:1.15")
}

val mainClassName by extra("$group.dronesecurity.userapplication.controller.Launcher")
val mainModuleName by extra("$group.dronesecurity.userapplication")

application {
    applicationDefaultJvmArgs = applicationDefaultJvmArgs.plus("--add-opens=javafx.graphics/javafx.scene=org.controlsfx.controls")
}
