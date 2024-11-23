plugins {
    id("java")
    id("application")
}

group = "dev.lysmux"
version = "1.0"

application {
    mainClass.set("dev.lysmux.lab3.Main")
}

tasks.jar {
    manifest.attributes("Main-Class" to application.mainClass)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

