package ru.woodymsk.socialapp.presentation.map_screen.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.presentation.map_screen.model.MapUiState
import ru.woodymsk.socialapp.presentation.map_screen.model.SuggestState
import ru.woodymsk.socialapp.presentation.map_screen.model.toTextStatus

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SearchTextField(
    state: MapUiState,
    onEvent: (MapEvents) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = state.query,
        onQueryChange = { newQuery ->
            onEvent(MapEvents.QueryUpdated(newQuery))
        },
        onSearch = {
            keyboardController?.hide()
            onEvent(MapEvents.StartSearch)
            active = false
        },
        active = active,
        onActiveChange = { isActive ->
            active = isActive
            if (!isActive) {
                keyboardController?.hide()
                onEvent(MapEvents.HideSuggestions)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.search_places)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.search_button)
            )
        },
        // clear button
        trailingIcon = {
            if (active) {
                IconButton(
                    onClick = {
                        onEvent(MapEvents.ClearSearchBar)
                        active = false
                        keyboardController?.hide()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            dividerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // suggestion list
        when (val suggestState = state.suggestState) {
            is SuggestState.Success -> {
                if (suggestState.items.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(suggestState.items) { suggest ->
                            SuggestItem(
                                suggest = suggest,
                                onClick = {
                                    keyboardController?.hide()
                                    onEvent(MapEvents.SuggestItemClicked(suggest))
                                    active = false
                                }
                            )
                        }
                    }
                }
            }

            is SuggestState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            SuggestState.Error -> {
                MapEvents.Error(SuggestState.Error.toTextStatus())
            }

            SuggestState.Off -> {}
        }
    }
}