import de.undercouch.gradle.tasks.download.Download

plugins {
    id("de.undercouch.download") version "5.5.0"
    id("io.freefair.lombok") version "9.0.0"
    id("java")
    war
}
//apply(from = "gradle/wildfly.gradle.kts")

group = "dev.lysmux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":tags"))
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("com.h2database:h2:2.3.232")
    implementation("org.liquibase:liquibase-core:4.33.0")

    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
    implementation("org.jboss.weld:weld-core-impl:6.0.3.Final")

    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.2")
    runtimeOnly("org.glassfish.web:jstl-impl:1.2")

    implementation("com.google.code.gson:gson:2.13.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.hibernate.validator:hibernate-validator:9.0.1.Final")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("lab2.war")

    exclude("webpack/")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

val wildflyVersion = "30.0.0.Final"
val wildflyBaseUrl = "https://github.com/wildfly/wildfly/releases/download"
val wildflyUrl = "$wildflyBaseUrl/$wildflyVersion/wildfly-$wildflyVersion.zip"
val wildflyDir = "$buildDir/wildfly"  // Теперь без версии
val wildflyZip = "$buildDir/wildfly-$wildflyVersion.zip"

val downloadWildfly by tasks.register<Download>("downloadWildfly") {
    group = "WildFly"

    src(wildflyUrl)
    dest(File(wildflyZip))
    overwrite(false)
    onlyIfModified(true)

    onlyIf { !isWildflyInstalled() }

    doFirst {
        println("Загрузка WildFly $wildflyVersion...")
        println("URL: $wildflyUrl")
    }

    doLast {
        println("WildFly успешно скачан: $wildflyZip")
    }
}

fun isWildflyInstalled(): Boolean {
    return File("$wildflyDir/bin/standalone.sh").exists() ||
            File("$wildflyDir/bin/standalone.bat").exists()
}

val extractWildfly by tasks.register<Copy>("extractWildfly") {
    group = "WildFly"
    description = "Распаковать архив WildFly"

    onlyIf { !isWildflyInstalled() }
    dependsOn(downloadWildfly)
    from(zipTree(downloadWildfly.dest))

    eachFile {
        val segments = this.path.split("/")
        if (segments.isNotEmpty() && segments[0].startsWith("wildfly-")) {
            this.path = segments.drop(1).joinToString("/")
        }
    }
    includeEmptyDirs = false

    into(wildflyDir)

    doFirst {
        println("Распаковка WildFly...")
        delete(wildflyDir)
    }

    doLast {
        println("WildFly распакован в: $wildflyDir")
    }
}

val setupWildfly by tasks.register("setupWildfly") {
    group = "WildFly"
    description = "Скачать и установить WildFly в папку build"

    onlyIf { !isWildflyInstalled() }
    dependsOn(extractWildfly)

    doLast {
        println("=".repeat(50))
        println("WildFly $wildflyVersion успешно установлен!")
        println("Каталог: $wildflyDir")
        println()
        println("Для запуска сервера выполните:")
        println("  Linux/Mac:   $wildflyDir/bin/standalone.sh")
        println("  Windows:     $wildflyDir\\bin\\standalone.bat")
        println("=".repeat(50))
    }
}

val runWildfly by tasks.register<Exec>("runWildfly") {
    group = "WildFly"
    description = "Запустить WildFly сервер"

    dependsOn(setupWildfly)

    workingDir(File(wildflyDir))
    if (System.getProperty("os.name").lowercase().contains("windows")) {
        commandLine("cmd", "/c", "bin\\standalone.bat")
    } else {
        commandLine("./bin/standalone.sh")
    }
}

val stopWildfly by tasks.register("stopWildfly") {
    group = "WildFly"
    description = "Остановить WildFly сервер"

    doLast {
        println("Остановка WildFly...")
        println("Для остановки сервера нажмите Ctrl+C в консоли где запущен runWildfly")
    }
}

val deployWildfly by tasks.register<Copy>("deployWildfly") {
    group = "WildFly"
    description = "Развернуть WAR файл в WildFly"

    dependsOn(setupWildfly, tasks.war)

    from(tasks.findByName("war")?.outputs?.files ?: file("build/libs/"))
    into("$wildflyDir/standalone/deployments")
    include("*.war")

    doFirst {
        println("Развертывание WAR в WildFly...")
    }

    doLast {
        println("WAR файл развернут в WildFly")
    }
}

val cleanWildfly by tasks.register<Delete>("cleanWildfly") {
    group = "WildFly"
    description = "Удалить установленный WildFly"

    delete(wildflyDir)
    delete(wildflyZip)

    doLast {
        println("WildFly удален из папки build")
    }
}

tasks.register<Exec>("deployRemote") {
    group = "WildFly"

    dependsOn(setupWildfly, tasks.war)

    workingDir = File("$wildflyDir/bin")
    executable = "powershell"
    args(
        "-File",
        "jboss-cli.ps1",
        "--connect",
        "--controller=localhost:5289",
        "--user=admin",
        "--password=\"jkh6QAl2kj8u!;l138\"",
        "--command=\"deploy --force ${tasks.war.get().archiveFile.get().asFile.absolutePath}\""
    )

    standardOutput = System.out
    errorOutput = System.err
}

tasks.clean {
    dependsOn(cleanWildfly)
}
