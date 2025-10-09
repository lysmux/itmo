// gradle/wildfly.gradle.kts

import de.undercouch.gradle.tasks.download.Download
import java.io.File

// Конфигурация WildFly
val wildflyVersion = "30.0.0.Final"
val wildflyBaseUrl = "https://github.com/wildfly/wildfly/releases/download"
val wildflyUrl = "$wildflyBaseUrl/$wildflyVersion/wildfly-$wildflyVersion.zip"
val wildflyDir = "$buildDir/wildfly-$wildflyVersion"
val wildflyZip = "$buildDir/wildfly-$wildflyVersion.zip"

// Проверяем, установлен ли уже WildFly

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

// Задача для распаковки WildFly
val extractWildfly by tasks.register<Copy>("extractWildfly") {
    group = "WildFly"
    description = "Распаковать архив WildFly"

    onlyIf { !isWildflyInstalled() }
    dependsOn(downloadWildfly)
    from(zipTree(downloadWildfly.dest))

    // Ключевое изменение: распаковываем содержимое папки wildfly-version в build/wildfly
    eachFile {
        // Убираем префикс с версией из пути
        val segments = this.path.split("/")
        if (segments.isNotEmpty() && segments[0].startsWith("wildfly-")) {
            this.path = segments.drop(1).joinToString("/")
        }
    }
    includeEmptyDirs = false

    into(wildflyDir)

    doFirst {
        println("Распаковка WildFly...")
        // Очищаем целевую директорию перед распаковкой
        delete(wildflyDir)
    }

    doLast {
        println("WildFly распакован в: $wildflyDir")
    }
}

// Основная задача установки
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

// Задача для быстрого запуска WildFly
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

// Задача для остановки WildFly (отправка сигнала остановки)
val stopWildfly by tasks.register("stopWildfly") {
    group = "WildFly"
    description = "Остановить WildFly сервер"

    doLast {
        println("Остановка WildFly...")
        // Здесь можно добавить логику остановки через jboss-cli
        println("Для остановки сервера нажмите Ctrl+C в консоли где запущен runWildfly")
    }
}

// Задача для деплоя WAR файла
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

// Задача для очистки
val cleanWildfly by tasks.register<Delete>("cleanWildfly") {
    group = "WildFly"
    description = "Удалить установленный WildFly"

    delete(wildflyDir)
    delete(wildflyZip)

    doLast {
        println("WildFly удален из папки build")
    }
}

// Интеграция со стандартной задачей clean
tasks.clean {
    dependsOn(cleanWildfly)
}

// Расширение для конфигурации из основного build.gradle.kts
extra["wildflyVersion"] = wildflyVersion
extra["wildflyDir"] = wildflyDir