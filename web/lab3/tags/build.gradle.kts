plugins {
    id("io.freefair.lombok") version "9.0.0"
    id("java")
}

group = "dev.lysmux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.faces:jakarta.faces-api:4.1.2")
    implementation("jakarta.el:jakarta.el-api:6.0.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}