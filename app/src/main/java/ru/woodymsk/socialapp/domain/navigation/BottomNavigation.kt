package ru.woodymsk.socialapp.domain.navigation

import ru.woodymsk.socialapp.presentation.common.TabTag

interface BottomNavigation {
    fun openTab(tabTag: TabTag)
    fun openTabWithNavigationReset(tabTag: TabTag)
}