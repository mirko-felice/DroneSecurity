plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
//    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.0.5")
    implementation("de.jjohannes.gradle:extra-java-module-info:0.10")
    implementation("org.openjfx:javafx-plugin:0.0.11")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

kotlinDslPluginOptions {
    jvmTarget.set("11")
}
