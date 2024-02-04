import java.util.Properties

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:5.2.5")
    implementation("de.jjohannes.gradle:extra-java-module-info:0.15")
    implementation("org.openjfx:javafx-plugin:0.0.13")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.4.0.2513")
    implementation("com.lordcodes.turtle:turtle:0.7.0")
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
