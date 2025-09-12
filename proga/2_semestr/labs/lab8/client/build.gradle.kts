plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.12.2.1"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
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
    mainClass.set("dev.lysmux.lab8.client.Main")
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

javafx {
    version = "17.0.12"
    modules = listOf("javafx.controls", "javafx.fxml")
}

jlink {
    imageZip.set(file("${buildDir}/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}

