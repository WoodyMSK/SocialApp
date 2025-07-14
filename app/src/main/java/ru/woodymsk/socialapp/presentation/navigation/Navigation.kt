package ru.woodymsk.socialapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event.compose.EventListView
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.PopBackStack
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.EventListScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.NewEventScreen
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel
import ru.woodymsk.socialapp.presentation.new_event.compose.NewEventView
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import kotlin.reflect.typeOf

private const val EVENT_VM_KEY = "EventViewModel"
private const val NEW_EVENT_VM_KEY = "NewEventViewModel"

@Composable
fun Navigation(
    navHostController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
) {

    // TODO bug, при переходе EventScreen -> NewEventScreen -> EventScreen -> NewEventScreen на последний экран переход не осуществляется

    NavHost(
        navController = navHostController,
        startDestination = EventListScreen,
    ) {
        Log.e("NavHost", "start NavHost")
        composable<EventListScreen> {
            Log.d("NavHost", "start EventListScreen")
            val viewModel: EventListViewModel = viewModel(
                factory = viewModelFactory,
                key = EVENT_VM_KEY
            )
            EventListView(viewModel) { navigateTo ->
                Log.d("NavHost", "EventListScreen navigateTo = $navigateTo")
                navHostController.navigate(navigateTo)
            }
        }

        composable<NewEventScreen>(
            typeMap = mapOf(
                typeOf<Event?>() to CustomNavType.EventType
            )
        ) {
            Log.d("NavHost", "start NewEventScreen")
            val arguments = it.toRoute<NewEventScreen>()
            val viewModel: NewEventViewModel = viewModel(
                factory = viewModelFactory,
                key = NEW_EVENT_VM_KEY,
            )

            LaunchedEffect(Unit) {
                Log.d("NavHost", "arguments.event = " + arguments.event.toString())
                viewModel.onEvent(NewEventEvents.EditEvent(arguments.event))
            }

            NewEventView(viewModel) { navigateTo ->
                Log.d("NavHost", "NewEventScreen navigateTo = $navigateTo")
                navHostController.navigate(navigateTo)
            }
        }

        composable<PopBackStack> {
            Log.d("NavHost", "navHostController.navigateUp()")
            navHostController.navigateUp()
        }
    }
}