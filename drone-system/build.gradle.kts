plugins {
    id("configuration")
}

dependencies  {
    implementation(libs.annotations)
    implementation(libs.junit)
    implementation("org.apache.commons:commons-exec:1.3")
    implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:1.5.4")
}

application {
    mainClass.set("drone.Activation")
}
