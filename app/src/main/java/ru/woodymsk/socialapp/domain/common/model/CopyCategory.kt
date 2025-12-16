package ru.woodymsk.socialapp.domain.common.model

import ru.woodymsk.socialapp.presentation.common.GrammaticalForm

data class CopyCategory(
    val name: Int,
    val grammaticalForm: GrammaticalForm,
)