plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.0-RC"
}

repositories {
    mavenCentral()
}

subprojects {
    tasks.register<Codingame>("codingame")
}