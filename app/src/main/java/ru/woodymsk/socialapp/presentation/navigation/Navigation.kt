package ru.woodymsk.socialapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event.compose.EventListView
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.EventScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.NewEventScreen
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel
import ru.woodymsk.socialapp.presentation.new_event.compose.NewEventView

private const val EVENT_VM_KEY = "EventViewModel"
private const val NEW_EVENT_VM_KEY = "NewEventViewModel"

@Composable
fun Navigation(
    navHostController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
) {

    NavHost(
        navController = navHostController,
        startDestination = EventScreen,
    ) {

        composable<EventScreen> {
            val viewModel: EventListViewModel = viewModel(
                factory = viewModelFactory,
                key = EVENT_VM_KEY
            )
            EventListView(viewModel) { navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }

        composable<NewEventScreen> {
            val viewModel: NewEventViewModel = viewModel(
                factory = viewModelFactory,
                key = NEW_EVENT_VM_KEY
            )
            NewEventView(viewModel) { navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }
    }
}