plugins {
    id("kotlin")
}

repositories {
    mavenCentral()
}

tasks.test { useJUnitPlatform() }

dependencies {
    implementation(lib.kotlin.stdlib)

    testImplementation(test.kotest)
    testImplementation(test.mockk)
    testImplementation(test.truth)
}
