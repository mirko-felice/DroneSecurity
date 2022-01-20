plugins {
    id("configuration")
    id("org.openjfx.javafxplugin") version "0.0.10"
}

dependencies  {
    implementation(libs.annotations)
    implementation(libs.junit)
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

tasks.jar {
    manifest {
        attributes(Pair("Main-Class", "controller.Launcher"))
    }
}