package ru.woodymsk.socialapp.presentation.map_screen.model

import com.yandex.mapkit.SpannableString

open class SuggestHolderItem(
    val title: SpannableString,
    val subtitle: SpannableString?,
    val onClick: () -> Unit,
)