plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies  {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("io.vertx:vertx-web:4.2.2")
}

tasks.wrapper {
    gradleVersion = "7.3"
}

tasks.test {
    useJUnitPlatform()
}
