package dev.lysmux.solver.ui

import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.black
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.cyan
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.Session

enum class Mode {
    RAW,
    FILE,
    RANDOM,
}

fun Session.selectMode(): Mode {
    var cursorIndex by liveVarOf(0)

    section {
        cyan { bold { textLine("Select mode:") } }

        Mode.entries.forEachIndexed { i, mode ->
            text(if (i == cursorIndex) '>' else ' '); text(' ')
            textLine(mode.toString())
        }

        textLine()
        black(isBright = true) { textLine("Use UP/DOWN to choose and ENTER to select") }
        textLine()
    }.runUntilSignal {
        onKeyPressed {
            when (key) {
                Keys.UP -> cursorIndex -= 1
                Keys.DOWN -> cursorIndex += 1
                Keys.ENTER -> signal()
            }

            if (cursorIndex < 0) cursorIndex = Mode.entries.lastIndex
            else if (cursorIndex > Mode.entries.lastIndex) cursorIndex = 0
        }
    }

    return Mode.entries[cursorIndex]
}