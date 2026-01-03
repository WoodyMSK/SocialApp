package ru.woodymsk.socialapp.presentation.common

import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.common.GrammaticalFormatter
import ru.woodymsk.socialapp.domain.common.model.CopyCategory
import javax.inject.Inject

class GrammaticalFormatterImpl @Inject constructor(
    private val resourceService: ResourcesService
) : GrammaticalFormatter {
    override fun getCopyMessage(category: CopyCategory): String {
        val name = resourceService.getString(category.name)
        val verbConjugation = getVerbConjugation(category.grammaticalForm)
        return "$name $verbConjugation"
    }

    private fun getVerbConjugation(form: GrammaticalForm): String = resourceService.getString(
        when (form) {
            GrammaticalForm.MASCULINE -> R.string.copied_masculine
            GrammaticalForm.FEMININE -> R.string.copied_feminine
            GrammaticalForm.NEUTER -> R.string.copied_neuter
            GrammaticalForm.PLURAL -> R.string.copied_plural
        })
}

enum class GrammaticalForm {
    MASCULINE,
    FEMININE,
    NEUTER,
    PLURAL,
}