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
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.domain.event.model.Event
import ru.woodymsk.socialapp.presentation.event.EventListViewModel
import ru.woodymsk.socialapp.presentation.event.compose.EventListView
import ru.woodymsk.socialapp.presentation.event_details.EventDetailsViewModel
import ru.woodymsk.socialapp.presentation.event_details.compose.EventDetailsView
import ru.woodymsk.socialapp.presentation.event_details.model.EventDetailsEvents
import ru.woodymsk.socialapp.presentation.map_screen.MapScreenViewModel
import ru.woodymsk.socialapp.presentation.map_screen.compose.MapScreenView
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.EventDetailsScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.EventListScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.MapScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.NewEventScreen
import ru.woodymsk.socialapp.presentation.navigation.model.Screen.PopBackStack
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel
import ru.woodymsk.socialapp.presentation.new_event.compose.NewEventView
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents
import kotlin.reflect.typeOf

private const val EVENT_VM_KEY = "EventViewModel"
private const val NEW_EVENT_VM_KEY = "NewEventViewModel"
private const val EVENT_DETAILS_VM_KEY = "EventDetailsViewModel"
private const val MAP_VM_KEY = "MapViewModel"
private const val OBJECT_COORDS = "objectСoords"

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
            val savedStateHandle = it.savedStateHandle
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

            LaunchedEffect(savedStateHandle) {
                savedStateHandle.getStateFlow<Coords?>(
                    OBJECT_COORDS,
                    null
                )
                    .collect { coords ->
                        coords?.let { newCoords ->
                            viewModel.onEvent(NewEventEvents.CoordsUpdated(newCoords))
                            // clear savedStateHandle
                            savedStateHandle.remove<Coords>(OBJECT_COORDS)
                        }
                    }
            }
        }

        composable<EventDetailsScreen> {
            val viewModel: EventDetailsViewModel = viewModel(
                factory = viewModelFactory,
                key = EVENT_DETAILS_VM_KEY
            )
            val arguments = it.toRoute<EventDetailsScreen>()

            LaunchedEffect(Unit) {
                viewModel.onEvent(EventDetailsEvents.LoadEvent(arguments.id))
            }

            EventDetailsView(viewModel) { navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }

        composable<MapScreen> {
            Log.d("NavHost", "start MapScreen")
            val viewModel: MapScreenViewModel = viewModel(
                factory = viewModelFactory,
                key = MAP_VM_KEY
            )

            MapScreenView(
                viewModel = viewModel,
                onNavigateTo = { navigateTo ->
                    Log.d("NavHost", "MapScreen navigateTo = $navigateTo")
                    navHostController.navigate(navigateTo)
                },
                onBackWithResult = { coords ->
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(OBJECT_COORDS, coords)

                    navHostController.navigateUp()
                }
            )
        }

        composable<PopBackStack> {
            Log.d("NavHost", "navHostController.navigateUp()")
            navHostController.navigateUp()
        }
    }
}