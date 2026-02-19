plugins {
    application
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
}

group = "dev.lysmux"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("com.varabyte.kotter:kotter-jvm:1.2.1")
    implementation("org.apache.commons:commons-math3:3.6.1")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("dev.lysmux.solver.MainKt")
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

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}