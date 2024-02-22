package ru.woodymsk.socialapp.presentation.activity.model

import ru.woodymsk.socialapp.presentation.common.TabTag

sealed class CurrentTabState {
    data class TabSelected(val selectedTabTag: TabTag) : CurrentTabState()
    data class InitError(val error: Throwable) : CurrentTabState()
}
