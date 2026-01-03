package ru.woodymsk.socialapp.presentation.map_screen.model

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import ru.woodymsk.socialapp.domain.map_screen.model.ObjectDetails

private val DEFAULT_POSITION = CameraPosition(Point(55.753284, 37.622034), 13.0f, 0f, 0f)

data class MapUiState(
    val query: String = "",
    val searchState: SearchState = SearchState.Off,
    val suggestState: SuggestState = SuggestState.Off,
    val selectedObject: GeoObject? = null,
    val selectedObjectDetails: ObjectDetails? = null,
    val showDetailsDialog: Boolean = false,
    val useCurrentLocation: Boolean = true,
    val defaultCameraPosition: CameraPosition = DEFAULT_POSITION,
    val showToast: String? = null,
    val isLoading: Boolean = false,
)

sealed interface SearchState {
    object Off : SearchState
    object Loading : SearchState
    object Error : SearchState
    data class Success(
        val items: List<SearchResponseItem>,
        val zoomToItems: Boolean,
        val itemsBoundingBox: BoundingBox,
    ) : SearchState
}

sealed interface SuggestState {
    data class Success(val items: List<SuggestHolderItem>) : SuggestState
    object Off : SuggestState
    object Loading : SuggestState
    object Error : SuggestState
}

fun SearchState.toTextStatus(): String {
    return when (this) {
        is SearchState.Error -> "Search error"
        is SearchState.Loading -> "Loading search results"
        is SearchState.Off -> "Search is off"
        is SearchState.Success -> "Search success"
    }
}

fun SuggestState.toTextStatus(): String {
    return when (this) {
        is SuggestState.Error -> "Error of suggestion"
        is SuggestState.Loading -> "Loading a list of suggestions"
        is SuggestState.Off -> "List of suggestions has been hide"
        is SuggestState.Success -> "Suggestions list has been show successful"
    }
}