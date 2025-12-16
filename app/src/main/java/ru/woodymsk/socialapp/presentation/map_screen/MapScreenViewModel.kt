package ru.woodymsk.socialapp.presentation.map_screen

import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.SuggestType
import com.yandex.runtime.Error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.woodymsk.socialapp.domain.common.GrammaticalFormatter
import ru.woodymsk.socialapp.domain.toBoundingBox
import ru.woodymsk.socialapp.error.AppError.Companion.handleError
import ru.woodymsk.socialapp.presentation.common.BaseViewModel
import ru.woodymsk.socialapp.domain.common.model.CopyCategory
import ru.woodymsk.socialapp.domain.map_screen.converter.ObjectDetailsConverter
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.presentation.map_screen.model.MapUiState
import ru.woodymsk.socialapp.presentation.map_screen.model.SearchResponseItem
import ru.woodymsk.socialapp.presentation.map_screen.model.SearchState
import ru.woodymsk.socialapp.presentation.map_screen.model.SuggestHolderItem
import ru.woodymsk.socialapp.presentation.map_screen.model.SuggestState
import ru.woodymsk.socialapp.presentation.map_screen.model.toTextStatus
import ru.woodymsk.socialapp.presentation.navigation.model.Screen
import javax.inject.Inject

class MapScreenViewModel @Inject constructor(
    private val objectDetailsConverter: ObjectDetailsConverter,
    private val grammaticalFormatter: GrammaticalFormatter,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var searchSession: Session? = null
    private var currentRegion: VisibleRegion? = null
    private var shouldShowSuggestions = true
    private var zoomToSearchResult = false
    private val searchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private val suggestSession: SuggestSession = searchManager.createSuggestSession()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }
    private val searchSessionListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }
            val boundingBox = response.metadata.boundingBox ?: return

            _uiState.update {
                it.copy(
                    searchState = SearchState.Success(
                        items = items,
                        zoomToItems = zoomToSearchResult,
                        itemsBoundingBox = boundingBox,
                    ),
                    suggestState = SuggestState.Off,
                )
            }
        }

        override fun onSearchError(error: Error) {
            handleError(SearchState.Error.toTextStatus())
        }
    }

    private val suggestSessionListener = object : SuggestSession.SuggestListener {
        override fun onResponse(response: List<SuggestItem>) {
            if (shouldShowSuggestions.not()) return

            val suggestItems = response.take(SUGGEST_NUMBER_LIMIT)
                .map { suggestItem ->
                    SuggestHolderItem(
                        title = suggestItem.title,
                        subtitle = suggestItem.subtitle,
                        onClick = {
                            shouldShowSuggestions = false
                            hideSuggestList()

                            val displayText = suggestItem.displayText ?: suggestItem.title.text
                            _uiState.update { it.copy(query = displayText) }

                            if (suggestItem.action == SuggestItem.Action.SEARCH) {
                                val uri = suggestItem.uri
                                if (uri != null) {
                                    submitUriSearch(uri)
                                } else {
                                    currentRegion?.let { region ->
                                        val searchText = suggestItem.searchText
                                        submitSearch(
                                            searchText,
                                            Geometry.fromBoundingBox(region.toBoundingBox())
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            _uiState.update { it.copy(suggestState = SuggestState.Success(suggestItems)) }
        }

        override fun onError(error: Error) {
            if (shouldShowSuggestions) {
                handleError(SuggestState.Error.toTextStatus())
            }
        }
    }

    fun onEvent(event: MapEvents) {
        when (event) {
            is MapEvents.QueryUpdated -> updateQuery(event.query)
            is MapEvents.VisibleRegionChanged -> setVisibleRegion(event.visibleRegion)
            is MapEvents.SuggestItemClicked -> selectSuggestItem(event.item)
            is MapEvents.PlacemarkClicked -> {
                updateSelectedObject(event.geoObject)
                showDetailsDialog(
                    geoObject = event.geoObject,
                    objectDetailsConverter = objectDetailsConverter,
                )
            }
            is MapEvents.ShowCopyToast -> showCopyToast(event.copyCategory)
            is MapEvents.Error -> handleError(event.error)
            MapEvents.StartSearch -> startSearch()
            MapEvents.ClearSearchBar -> clearSearchBar()
            MapEvents.HideSuggestions -> hideSuggestList()
            MapEvents.HideToast -> hideToast()
            MapEvents.DismissDetailsDialog -> dismissDetailsDialog()
            MapEvents.GoToBack -> onBackPressed()
        }
    }

    private fun onBackPressed() {
        navigateTo(Screen.PopBackStack)
    }

    private fun updateQuery(query: String) {
        if (query != _uiState.value.query) {
            shouldShowSuggestions = true
        }

        _uiState.update { it.copy(query = query) }

        if (query.isNotEmpty() && shouldShowSuggestions) {
            submitSuggest(query)
        } else {
            hideSuggestList()
        }
    }

    private fun updateSelectedObject(geoObject: GeoObject) {
        _uiState.update { it.copy(selectedObject = geoObject) }
    }

    // Updating the visible area of the map for contextual search
    private fun setVisibleRegion(visibleRegion: VisibleRegion) {
        currentRegion = visibleRegion

        if (_uiState.value.searchState is SearchState.Success) {
            viewModelScope.launch(exceptionHandler) {
                searchSession?.let { it ->
                    it.setSearchArea(Geometry.fromBoundingBox(visibleRegion.toBoundingBox()))
                    it.resubmit(searchSessionListener)
                    _uiState.update { it.copy(searchState = SearchState.Loading) }
                    zoomToSearchResult = false
                }
            }
        }
    }

    // manually launching the search
    private fun startSearch() {
        val query = _uiState.value.query
        if (query.isEmpty()) return

        val region = currentRegion ?: return

        shouldShowSuggestions = false
        hideSuggestList()
        submitSearch(query, Geometry.fromBoundingBox(region.toBoundingBox()))
    }

    // Basic search for objects on the map based on a text query in a specified area
    private fun submitSearch(query: String, geometry: Geometry) {
        searchSession?.cancel()
        searchSession = searchManager.submit(
            query,
            geometry,
            SearchOptions().apply {
                resultPageSize = PAGE_SIZE
            },
            searchSessionListener
        )
        _uiState.update { it.copy(searchState = SearchState.Loading) }
        zoomToSearchResult = true
    }

    private fun submitUriSearch(uri: String) {
        searchSession?.cancel()
        searchSession = searchManager.searchByURI(
            uri,
            SearchOptions().apply {
                resultPageSize = PAGE_SIZE
            },
            searchSessionListener
        )
        _uiState.update { it.copy(searchState = SearchState.Loading) }
        zoomToSearchResult = true
    }

    private fun submitSuggest(
        query: String,
        options: SuggestOptions = SUGGEST_OPTIONS,
    ) {
        if (shouldShowSuggestions.not()) return

        val region = currentRegion ?: return
        suggestSession.suggest(query, region.toBoundingBox(), options, suggestSessionListener)
        _uiState.update { it.copy(suggestState = SuggestState.Loading) }
    }

    private fun searchByCoordinates(point: Point) {
        searchSession?.cancel()

        searchSession = searchManager.submit(
            point,
            null,
            SearchOptions().apply {
                resultPageSize = 5
            },
            searchSessionListener
        )

        _uiState.update { it.copy(searchState = SearchState.Loading) }
        zoomToSearchResult = true
    }

    private fun selectSuggestItem(item: SuggestHolderItem) {
        shouldShowSuggestions = false
        hideSuggestList()
        item.onClick()
    }

    private fun clearSearchBar() {
        searchSession?.cancel()
        searchSession = null
        shouldShowSuggestions = true
        _uiState.update {
            it.copy(
                query = "",
                searchState = SearchState.Off,
                suggestState = SuggestState.Off,
                selectedObject = null,
                showDetailsDialog = false
            )
        }
        hideSuggestList()
    }

    private fun hideSuggestList() {
        suggestSession.reset()
        _uiState.update { it.copy(suggestState = SuggestState.Off) }
    }

    private fun showDetailsDialog(
        geoObject: GeoObject,
        objectDetailsConverter: ObjectDetailsConverter,
    ) {
        shouldShowSuggestions = false
        hideSuggestList()
        _uiState.update {
            val objectDetails = objectDetailsConverter.convertGeoObjectToObjectDetails(geoObject)
            it.copy(
                selectedObjectDetails = objectDetails,
                showDetailsDialog = true,
            )
        }
    }

    private fun dismissDetailsDialog() {
        _uiState.update {
            it.copy(
                showDetailsDialog = false,
                selectedObject = null,
                selectedObjectDetails = null,
            )
        }
    }

    private fun showCopyToast(copyCategory: CopyCategory?) {
        val message = copyCategory?.let { grammaticalFormatter.getCopyMessage(copyCategory) }
        showToast(message)
    }

    private fun handleError(error: String?) {
        showToast(error)
    }

    private fun showToast(text: String?) {
        _uiState.update { it.copy(showToast = text) }
    }

    private fun hideToast() {
        _uiState.update { it.copy(showToast = null) }
    }

    companion object {
        private const val SUGGEST_NUMBER_LIMIT = 20
        private const val PAGE_SIZE = 32
        private val SUGGEST_OPTIONS = SuggestOptions().setSuggestTypes(
            SuggestType.GEO.value
                    or SuggestType.BIZ.value
                    or SuggestType.TRANSIT.value
        )
    }
}