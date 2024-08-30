plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20240303")
    implementation("org.json:json:20240303")
    implementation("com.renomad:minum:8.0.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "com.nospawnn.githubactivitytracker.GithubActivityTracker"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

distributions {
    main {
        contents {
            into("bin/src/main/resources") {
                from("src/main/resources")
            }
        }
    }
}