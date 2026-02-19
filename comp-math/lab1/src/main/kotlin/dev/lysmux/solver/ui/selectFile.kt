package dev.lysmux.solver.ui

import com.varabyte.kotter.foundation.input.Completions
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.input
import com.varabyte.kotter.foundation.input.onInputEntered
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.input.runUntilInputEntered
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.black
import com.varabyte.kotter.foundation.text.cyan
import com.varabyte.kotter.foundation.text.green
import com.varabyte.kotter.foundation.text.red
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.foundation.text.yellow
import com.varabyte.kotter.runtime.Session
import dev.lysmux.solver.domain.EquationSystem
import dev.lysmux.solver.domain.toEquationSystem
import java.io.File
import kotlin.math.min
import kotlin.system.exitProcess

const val VISIBLE_SIZE = 30

fun File.listFilesOrEmpty(): Array<File> = listFiles() ?: emptyArray()

fun Session.selectFile(): File {
    var currentDir = File(".").canonicalFile
    var contents by liveVarOf(currentDir.listFilesOrEmpty())
    var cursorIndex by liveVarOf(0)

    section {
        green { textLine("Current directory: ${currentDir.absolutePath}") }
        textLine()

        if (contents.isEmpty()) yellow { textLine("No files found") }

        val start = (cursorIndex - VISIBLE_SIZE + 1).coerceAtLeast(0)
        val end = cursorIndex
            .coerceAtMost(contents.lastIndex)
            .coerceAtLeast(min(contents.lastIndex, VISIBLE_SIZE))
        val toShow = contents.slice(start..end)
        toShow.forEachIndexed { i, file ->
            text(if (i == cursorIndex.coerceAtMost(VISIBLE_SIZE - 1)) '>' else ' '); text(' ')
            when {
                file.isDirectory -> cyan {
                    textLine(if (file.parent == null) file.absolutePath else file.name + "/")
                }

                else -> textLine(file.name)
            }
        }

        textLine()
        black(isBright = true) { textLine("Use UP/DOWN to choose, ENTER to select and ESC to go back") }
        textLine()
    }.runUntilSignal {
        onKeyPressed {
            when (key) {
                Keys.UP -> cursorIndex -= 1
                Keys.DOWN -> cursorIndex += 1
                Keys.ESC -> {
                    if (currentDir.parentFile != null) {
                        currentDir = currentDir.parentFile
                        contents = currentDir.listFilesOrEmpty()
                        cursorIndex = 0
                    } else {
                        val roots = File.listRoots()
                        contents = roots
                        cursorIndex = 0
                    }
                }

                Keys.ENTER -> {
                    val selectedFile = contents[cursorIndex]
                    if (selectedFile.isDirectory) {
                        currentDir = selectedFile
                        contents = currentDir.listFilesOrEmpty()
                        cursorIndex = 0
                    } else signal()
                }
            }

            if (cursorIndex < 0) cursorIndex = contents.lastIndex
            else if (cursorIndex > contents.lastIndex) cursorIndex = 0
        }
    }

    return contents[cursorIndex]
}

fun Session.fromFile(): EquationSystem {
    while (true) {
        val file = selectFile()
        try {
            return file.toEquationSystem()
        } catch (e: Exception) {
            section {
                red { textLine("Error: ${e.message}") }
                textLine()
                textLine("Would you like to try again? (Y/n)")
                input(Completions("yes", "no"), initialText = "y")
            }.runUntilInputEntered {
                onInputEntered {
                    if (!"yes".startsWith(input.lowercase())) exitProcess(0)
                }
            }
        }
    }
}