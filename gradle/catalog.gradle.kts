dependencyResolutionManagement {
    versionCatalogs {
        create("lib") {
            version("kotlin", "1.7.20")
            alias("kotlin-stdlib").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
        }

        create("test") {
            version("kotest", "5.3.2")
            alias("kotest").to("io.kotest", "kotest-runner-junit5").versionRef("kotest")

            version("mockk", "1.12.4")
            alias("mockk").to("io.mockk", "mockk").versionRef("mockk")

            version("truth", "1.1.3")
            alias("truth").to("com.google.truth", "truth").versionRef("truth")
        }
    }
}
