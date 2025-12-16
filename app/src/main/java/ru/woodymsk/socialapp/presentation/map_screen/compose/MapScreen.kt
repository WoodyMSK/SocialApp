package ru.woodymsk.socialapp.presentation.map_screen.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.presentation.map_screen.model.MapUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    state: MapUiState,
    onEvent: (MapEvents) -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // map
            MapViewContainer(
                state = state,
                modifier = Modifier.fillMaxSize(),
                onEvent = onEvent,
            )

            // search bar
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                SearchTextField(
                    state = state,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
@Preview
fun MapScreenPreview() {
    MaterialTheme {
        MapScreen(
            state = MapUiState(),
            onEvent = {}
        )
    }
}