package ru.woodymsk.socialapp.presentation.map_screen.model

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.map.VisibleRegion
import ru.woodymsk.socialapp.domain.common.model.CopyCategory

sealed class MapEvents {
    data class QueryUpdated(val query: String) : MapEvents()
    data class VisibleRegionChanged(val visibleRegion: VisibleRegion) : MapEvents()
    data class SuggestItemClicked(val item: SuggestHolderItem) : MapEvents()
    data class PlacemarkClicked(val geoObject: GeoObject) : MapEvents()
    data class ShowCopyToast(val copyCategory: CopyCategory?) : MapEvents()
    data class Error(val error: String?) : MapEvents()
    object StartSearch : MapEvents()
    object ClearSearchBar : MapEvents()
    object HideSuggestions : MapEvents()
    object HideToast : MapEvents()
    object DismissDetailsDialog : MapEvents()
    object GoToBack : MapEvents()
}