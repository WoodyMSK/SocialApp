package ru.woodymsk.socialapp.presentation.event.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import ru.woodymsk.socialapp.presentation.navigation.Navigation


@Composable
fun EventScreenContainer(viewModelFactory: ViewModelProvider.Factory) {
    Navigation(
        navHostController = rememberNavController(),
        viewModelFactory = viewModelFactory,
    )
}