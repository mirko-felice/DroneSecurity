plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
//    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.0.5")
    implementation("de.jjohannes.gradle:extra-java-module-info:0.10")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
