package dev.lysmux.solver.ui

import com.varabyte.kotter.foundation.input.input
import com.varabyte.kotter.foundation.input.onInputChanged
import com.varabyte.kotter.foundation.input.onInputEntered
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.red
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.runtime.Session
import dev.lysmux.solver.domain.MAX_SIZE

fun Session.selectSize(): Int {
    var warning by liveVarOf<String?>(null)
    var size by liveVarOf(1)

    section {
        textLine("Enter matrix size:")
        text("> "); input(initialText = "3")

        warning?.let { warning ->
            textLine()
            red { textLine(warning) }
        }
    }.runUntilSignal {
        onInputChanged { if (input.isNotBlank() && input.toIntOrNull() == null) rejectInput() }
        onInputEntered {
            if (input.toIntOrNull() == null) return@onInputEntered
            size = input.toInt()

            when {
                size < 1 -> warning = "Matrix size must be positive"
                size > MAX_SIZE -> warning = "Matrix size must be less than $MAX_SIZE"
                else -> {
                    warning = null
                    signal()
                }
            }
        }
    }

    return size
}