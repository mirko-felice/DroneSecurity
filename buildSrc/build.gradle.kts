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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

kotlinDslPluginOptions {
    jvmTarget.set("11")
}
