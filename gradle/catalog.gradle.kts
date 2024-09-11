dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            version("kotlin", "2.0.20")
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
        }

        create("test") {
            version("kotest", "5.9.1")
            library("kotest", "io.kotest", "kotest-runner-junit5").versionRef("kotest")

            version("mockk", "1.13.12")
            library("mockk", "io.mockk", "mockk").versionRef("mockk")

            version("truth", "1.4.4")
            library("truth", "com.google.truth", "truth").versionRef("truth")
        }
    }
}
