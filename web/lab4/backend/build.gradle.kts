plugins {
    id("io.freefair.lombok") version "9.0.0"
    id("java")
    war
}

group = "dev.lysmux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    compileOnly("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
    compileOnly("jakarta.persistence:jakarta.persistence-api:3.2.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("jakarta.transaction:jakarta.transaction-api:2.0.1")

    implementation("jakarta.validation:jakarta.validation-api:4.0.0-M1")

    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.1")
    implementation("tools.jackson.jaxrs:jackson-jaxrs-json-provider:3.0.2")
    implementation("tools.jackson.core:jackson-databind:3.0.2")

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