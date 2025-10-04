import org.wildfly.build.gradle.provisioning.ProvisionTask

plugins {
    id("org.wildfly.build.provision") version "0.0.11"
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
    implementation("com.google.code.gson:gson:2.13.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("jakarta.websocket:jakarta.websocket-api:2.2.0")
    implementation("jakarta.websocket:jakarta.websocket-client-api:2.2.0")
    implementation("org.hibernate.validator:hibernate-validator:9.0.1.Final")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Copy>("deployToWildfly") {
    from(tasks.war.get().archiveFile)
    into("wildfly/standalone/deployments")
    dependsOn(tasks.war)
}

tasks.war {
    finalizedBy(tasks.getByName("deployToWildfly"))
}

tasks.withType<ProvisionTask>().configureEach {
    variables.put("wildfly.version", "37.0.1.Final");
}