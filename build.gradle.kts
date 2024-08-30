plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
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

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
        attributes["Class-Path"] = configurations
        archiveBaseName = "gat"
        destinationDirectory = file("$rootDir")
    }

    // fat jar
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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