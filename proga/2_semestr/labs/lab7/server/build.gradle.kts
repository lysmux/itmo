plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.12.2.1"
}

group = "dev.lysmux"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.apache.commons:commons-lang3:3.17.0")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("org.jline:jline:3.29.0")
    implementation("org.jline:jline-terminal-jansi:3.29.0")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    implementation("org.postgresql:postgresql:42.7.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.lysmux.lab7.server.Main")
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