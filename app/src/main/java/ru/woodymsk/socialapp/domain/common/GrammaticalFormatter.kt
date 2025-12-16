package ru.woodymsk.socialapp.domain.common

import ru.woodymsk.socialapp.domain.common.model.CopyCategory

interface GrammaticalFormatter {
    fun getCopyMessage(category: CopyCategory): String
}