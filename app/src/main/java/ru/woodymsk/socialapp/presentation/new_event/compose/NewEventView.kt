package ru.woodymsk.socialapp.presentation.new_event.compose

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import ru.woodymsk.socialapp.presentation.new_event.NewEventViewModel
import ru.woodymsk.socialapp.presentation.new_event.model.NewEventEvents

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NewEventView(
    viewModel: NewEventViewModel,
    onNavigateTo: (Screen) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        NewEventScreen(
            state = viewModel.uiState.value,
            onEvent = viewModel::onEvent,
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LaunchedEffect(Unit) {
        Log.d("NewEventView","LaunchedEffect(Unit), onBackPressed")
        viewModel.navigationEvents.collect { onNavigateTo(it) }
    }

    LaunchedEffect(uiState) {
        when {
            uiState.eventDataInvalid != null -> {
                uiState.eventDataInvalid?.let {
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                }
                viewModel.onEvent(NewEventEvents.DismissDataInvalid)
            }

            uiState.error !=null -> {
                uiState.error?.let {
                    Toast.makeText(
                        context,
                        it.code,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                viewModel.onEvent(NewEventEvents.Error(null))
                viewModel.onEvent(NewEventEvents.Loading(false))
            }
        }

    }
}