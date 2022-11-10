plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

repositories {
    mavenCentral()
}

subprojects {
    tasks.register<Codingame>("codingame")
}