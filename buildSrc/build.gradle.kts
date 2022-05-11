import java.util.Properties

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:5.0.5")
    implementation("de.jjohannes.gradle:extra-java-module-info:0.10")
    implementation("org.openjfx:javafx-plugin:0.0.11")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3")
    implementation("com.lordcodes.turtle:turtle:0.6.0")
}

val javaVersion: String = file(rootDir.parentFile.path + File.separator + "gradle.properties").inputStream().use {
    val gradleProperties = Properties()
    gradleProperties.load(it)
    gradleProperties.getProperty("java.version", "11")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

kotlinDslPluginOptions {
    jvmTarget.set(javaVersion)
}
