plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(lib.kotlin.stdlib)
    implementation(project(":Geometry"))

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
}
