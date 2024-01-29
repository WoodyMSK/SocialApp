package ru.woodymsk.socialapp.domain.navigation.subnavigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import ru.woodymsk.socialapp.presentation.common.TabTag
import javax.inject.Inject

class LocalCiceroneHolder @Inject constructor() {
    private val containers = HashMap<TabTag, Cicerone<Router>>()

    fun getCicerone(containerTag: TabTag): Cicerone<Router> =
        containers.getOrPut(containerTag) {
            create()
        }

    fun clear() {
        containers.clear()
    }
}