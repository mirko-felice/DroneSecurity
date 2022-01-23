plugins {
    id("configuration")
}

dependencies  {
    implementation(libs.annotations)
    testImplementation(libs.jupiter)
    implementation("org.apache.commons:commons-exec:1.3")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:1.5.4")
    implementation("com.google.code.gson:gson:2.8.6")
}

application {
    mainClass.set("$group.dronesecurity.dronesystem.Activation")
    mainModule.set("$group.dronesecurity.dronesystem")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "$group.dronesecurity.dronesystem.Activation"
        attributes["Automatic-Module-Name"] = "$group.dronesecurity.dronesystem"
    }
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fat")
    from(sourceSets.main.get().output)
    dependsOn(configurations.compileClasspath)
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
    manifest {
        attributes["Main-Class"] = "it.unibo.dronesecurity.dronesystem.Activation"
        attributes["Automatic-Module-Name"] = "it.unibo.dronesecurity.dronesystem"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

extraJavaModuleInfo {
    module("commons-exec-1.3.jar", "org.apache.commons.exec", "1.3") {
        exports("org.apache.commons.exec")
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