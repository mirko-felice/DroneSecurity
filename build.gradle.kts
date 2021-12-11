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
}

tasks.test {
    useJUnitPlatform()
}

checkstyle {
    isIgnoreFailures = true
}

pmd {
    isIgnoreFailures = true
}

tasks.register("goAway") {
    doLast { println("Stay away from here!") }
}
