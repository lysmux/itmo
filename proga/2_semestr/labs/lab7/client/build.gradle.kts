plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.12.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("org.apache.commons:commons-lang3:3.17.0")

    implementation("org.jline:jline:3.29.0")
    implementation("org.jline:jline-terminal-jansi:3.29.0")
}

application {
    mainClass.set("dev.lysmux.lab7.client.Main")
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

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    sourceCompatibility = "21"
    targetCompatibility = "21"
}