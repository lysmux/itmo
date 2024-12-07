plugins {
    id("application")
    kotlin("jvm") version "2.1.0"
}

group = "dev.lysmux"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("dev.lysmux.lab3.MainKt")
}

tasks.jar {
    manifest.attributes("Main-Class" to application.mainClass)
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}