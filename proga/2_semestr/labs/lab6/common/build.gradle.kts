plugins {
    id("java")
    id("io.freefair.lombok") version "8.12.2.1"
}

group = "dev.lysmux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    implementation("org.jline:jline:3.29.0")
    implementation("org.jline:jline-terminal-jansi:3.29.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}