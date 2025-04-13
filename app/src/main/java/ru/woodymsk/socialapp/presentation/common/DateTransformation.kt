package ru.woodymsk.socialapp.presentation.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// visual transformation for mask display
class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(12)
        val formatted = buildString {
            digits.forEachIndexed { index, c ->
                when (index) {
                    2 -> append("/$c")   // День: после 2 символа
                    4 -> append("/$c")   // Месяц: после 4 символа
                    8 -> append(" $c")   // Пробел после года
                    10 -> append(":$c")  // Двоеточие после часов
                    else -> append(c)
                }
            }
        }
        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = DateOffsetMapping,
        )
    }
}

// correct cursor offset
object DateOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return when {
            offset <= 2 -> offset
            offset <= 4 -> offset + 1
            offset <= 8 -> offset + 2
            offset <= 10 -> offset + 3
            else -> (offset + 4).coerceAtMost(16)
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return when {
            offset <= 2 -> offset
            offset <= 5 -> (offset - 1).coerceAtLeast(0)
            offset <= 10 -> (offset - 2).coerceAtLeast(0)
            offset <= 13 -> (offset - 3).coerceAtLeast(0)
            else -> (offset - 4).coerceAtLeast(0)
        }.coerceAtMost(12)
    }
}