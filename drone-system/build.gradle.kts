plugins {
    id("configuration")
}

val apacheCommonsVersion = "1.3"
val awsCrtVersion = "0.15.15"
val awsIotVersion = "1.5.4"

dependencies  {
    implementation(libs.annotations)
    testImplementation(libs.jupiter)
    implementation("org.apache.commons:commons-exec:$apacheCommonsVersion")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:$awsIotVersion")
    implementation("software.amazon.awssdk.crt:aws-crt:$awsCrtVersion")
    implementation("com.google.code.gson:gson:2.8.6")
}

val mainClassName by extra("$group.dronesecurity.dronesystem.Activation")
val mainModuleName by extra("$group.dronesecurity.dronesystem")

extraJavaModuleInfo {
    module("commons-exec-$apacheCommonsVersion.jar", "org.apache.commons.exec", apacheCommonsVersion) {
        exports("org.apache.commons.exec")
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