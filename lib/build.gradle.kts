import java.util.Properties

plugins {
    id("dronesecurity-library")
}

tasks {
    test {
        dependsOn("createCerts")
        finalizedBy("clearCerts")
    }
}
