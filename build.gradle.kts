plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

subprojects {
    tasks.register<Codingame>("codingame")
}
