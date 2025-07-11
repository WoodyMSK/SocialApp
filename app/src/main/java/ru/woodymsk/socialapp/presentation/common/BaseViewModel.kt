package ru.woodymsk.socialapp.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.presentation.navigation.model.Screen

abstract class BaseViewModel : ViewModel() {
    private val _navigationEvents = Channel<Screen>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    protected fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.send(screen)
        }
    }
}