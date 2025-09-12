plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.12.1"
}

group = "dev.lysmux"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // XML serialization
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.2")
}

application {
    mainClass.set("dev.lysmux.lab5.Main")
}

tasks.test {
    useJUnitPlatform()
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
    sourceCompatibility = "17"
    targetCompatibility = "17"
}
