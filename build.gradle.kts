plugins {
    java
    checkstyle
    pmd
}

repositories {
    mavenCentral()
}

tasks.wrapper {
    gradleVersion = "7.3"
}

dependencies  {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("io.vertx:vertx-web:4.2.1")
    implementation("io.vertx:vertx-web-openapi:4.2.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("goAway") {
    doLast { println("Stay away from here!") }
}
