plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.14.2"
}

group = "dev.lysmux"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/fastcgi-lib.jar"))
    implementation("com.google.code.gson:gson:2.13.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.lysmux.Main")
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
    sourceCompatibility = "17"
    targetCompatibility = "17"
}