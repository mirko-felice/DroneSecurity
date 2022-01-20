plugins {
    id("configuration")
}

dependencies  {
    implementation(libs.annotations)
    implementation(libs.junit)
}

application {
    mainClass.set("drone.Activation")
}
