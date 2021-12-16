plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies  {

}

tasks.wrapper {
    gradleVersion = "7.3"
}

tasks.register("goAway") {
    doLast { println("Stay away from here!") }
}
