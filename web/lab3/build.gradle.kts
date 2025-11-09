import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
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
    implementation(project(":tags"))

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("jakarta.faces:jakarta.faces-api:4.1.2")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:5.0.0-M1")
    implementation("org.primefaces:primefaces:15.0.9:jakarta")

    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("org.eclipse.persistence:org.eclipse.persistence.jpa:5.0.0-B11")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

node {
    version = "24.11.0"
    download = true
    nodeProjectDir.set(file("$projectDir/src/main/webapp/webpack"))
}

tasks.register<NpmTask>("webpack_build") {
    args = listOf("run", "build:dev")
}

tasks.war {
    archiveFileName.set("server.war")
    outputs.upToDateWhen { false }
    exclude("webpack/")
    dependsOn("webpack_build")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}