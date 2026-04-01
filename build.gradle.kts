import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.abhaykaushal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Coroutines core and flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
