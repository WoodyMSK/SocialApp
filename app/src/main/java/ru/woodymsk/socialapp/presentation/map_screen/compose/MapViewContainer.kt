package ru.woodymsk.socialapp.presentation.map_screen.compose

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.data.model.Coords
import ru.woodymsk.socialapp.presentation.common.compose.LaunchSettingsDialog
import ru.woodymsk.socialapp.presentation.common.compose.rememberPermissionsState
import ru.woodymsk.socialapp.presentation.map_screen.model.MapEvents
import ru.woodymsk.socialapp.presentation.map_screen.model.MapUiState
import ru.woodymsk.socialapp.presentation.map_screen.model.SearchResponseItem
import ru.woodymsk.socialapp.presentation.map_screen.model.SearchState
import ru.woodymsk.socialapp.presentation.map_screen.model.toTextStatus

private const val URI_PACKAGE_SCHEME = "package"

@Composable
fun MapViewContainer(
    state: MapUiState,
    modifier: Modifier = Modifier,
    onEvent: (MapEvents) -> Unit,
) {
    val context = LocalContext.current
    // map create
    val mapView = remember {
        MapView(context).apply {
            mapWindow.map.move(state.defaultCameraPosition)
        }
    }
    // camera movement listener
    val cameraListener = CameraListener { map, cameraPosition, cameraUpdateReason, finished ->
        if (finished && cameraUpdateReason == CameraUpdateReason.GESTURES) {
            onEvent(MapEvents.VisibleRegionChanged(map.visibleRegion))
        }
    }
    var showLocationSettingsDialog by remember { mutableStateOf(false) }
    var cameraPosition by remember { mutableStateOf<CameraPosition?>(null) }
    var userLocation by remember { mutableStateOf<Point?>(null) }
    val locationPermissionState = rememberPermissionsState(
        permissions = listOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
        ),
        onGrantedAction = {
            // get user location
            getCurrentLocation(context) { location ->
                location?.let {
                    userLocation = Point(it.lat, it.long)
                    val newCameraPosition = CameraPosition(
                        Point(it.lat, it.long),
                        15.0f,
                        0f,
                        0f,
                    )
                    cameraPosition = newCameraPosition

                    updatePlacemarks(
                        mapView = mapView,
                        items = null,
                        userLocation = userLocation,
                        onEvent = onEvent,
                    )
                }
            }
        },
        onDeniedAction = {
            // user refused - use default location
            cameraPosition = state.defaultCameraPosition
        },
        onPermanentlyDeniedAction = {
            // user permanently refused - show a dialog to go to settings
            showLocationSettingsDialog = true
            cameraPosition = state.defaultCameraPosition
        }
    )

    // initial visibility region
    LaunchedEffect(state.useCurrentLocation) {
        if (state.useCurrentLocation) {
            // checking for permission to use user location
            locationPermissionState.launchPermissionRequestsAndAction()
        } else {
            // use default location
            onEvent(MapEvents.VisibleRegionChanged(mapView.mapWindow.map.visibleRegion))
        }
    }

    // updating labels when the search status changes
    LaunchedEffect(state.searchState) {
        when (val searchState = state.searchState) {
            is SearchState.Success -> {
                updatePlacemarks(
                    mapView = mapView,
                    items = searchState.items,
                    userLocation = userLocation,
                    onEvent = onEvent,
                )
                if (searchState.zoomToItems) {
                    val points = searchState.items.map { it.point }
                    focusCamera(mapView, points, searchState.itemsBoundingBox)
                }
            }

            is SearchState.Error -> {
                onEvent(MapEvents.Error(SearchState.Error.toTextStatus()))
            }

            else -> {}
        }
    }

    // Updating camera when getting a location
    LaunchedEffect(cameraPosition) {
        cameraPosition?.let { position ->
            mapView.mapWindow.map.move(position, Animation(Animation.Type.SMOOTH, 1.0f), null)
            // after moving, update current region
            onEvent(MapEvents.VisibleRegionChanged(mapView.mapWindow.map.visibleRegion))
        }
    }

    // management life cycle of a map
    DisposableEffect(Unit) {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        mapView.mapWindow.map.addCameraListener(cameraListener)
        onDispose {
            mapView.mapWindow.map.removeCameraListener(cameraListener)
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    // map
    AndroidView(
        factory = { mapView },
        modifier = modifier,
    )

    // FAB
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 24.dp, bottom = 24.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = {
                locationPermissionState.launchPermissionRequestsAndAction()
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(
                painterResource(id = R.drawable.ic_location_arrow_24),
                stringResource(R.string.add_event)
            )
        }
    }

    // dialog to go to settings
    LaunchSettingsDialog(
        showDialog = showLocationSettingsDialog,
        title = stringResource(R.string.location_access_is_required),
        onDismiss = { showLocationSettingsDialog = false },
        onConfirm = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts(URI_PACKAGE_SCHEME, context.packageName, null)
            }
            context.startActivity(intent)
            showLocationSettingsDialog = false
        },
        onCancel = {
            showLocationSettingsDialog = false
            cameraPosition = state.defaultCameraPosition
        }
    )
}

private fun updatePlacemarks(
    mapView: MapView,
    items: List<SearchResponseItem>?,
    userLocation: Point?,
    onEvent: (MapEvents) -> Unit,
) {

    val mapObjects = mapView.mapWindow.map.mapObjects
    // clearing previous results
    mapObjects.clear()
    // user location icon
    val userLocationProvider = ImageProvider.fromResource(
        mapView.context,
        R.drawable.map_pin_red_149x149,
    )
    // search results icon
    val searchResultProvider = ImageProvider.fromResource(
        mapView.context,
        R.drawable.map_pin_blue_64x64,
    )
    val placemarkTapListener = MapObjectTapListener { mapObject, point ->
        val geoObject = mapObject.userData as? GeoObject
        geoObject?.let {
            onEvent(MapEvents.PlacemarkClicked(it))
        }
        true
    }

    items?.forEach { item ->
        mapObjects.addPlacemark().apply {
            geometry = item.point
            setIcon(searchResultProvider, IconStyle().apply {
                scale = 1f
            })
            addTapListener(placemarkTapListener)
            // save GeoObject for use when clicking on a placemark
            userData = item.geoObject
        }
    }

    userLocation?.let { location ->
        mapObjects.addPlacemark().apply {
            geometry = location
            setIcon(userLocationProvider, IconStyle().apply {
                scale = 0.85f
            })
        }
    }
}

private fun focusCamera(
    mapView: MapView,
    points: List<Point>,
    boundingBox: BoundingBox
) {
    if (points.isEmpty()) return

    val position = if (points.size == 1) {
        // for one object, we save the current zoom, azimuth, tilt
        mapView.mapWindow.map.cameraPosition.run {
            CameraPosition(points.first(), zoom, azimuth, tilt)
        }
    } else {
        // for several objects, we select a camera to cover bounding area
        mapView.mapWindow.map.cameraPosition(Geometry.fromBoundingBox(boundingBox))
    }

    mapView.mapWindow.map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)
}

private fun getCurrentLocation(
    context: Context,
    onLocationResult: (Coords?) -> Unit
) {
    val locationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    onLocationResult(Coords(it.latitude, it.longitude))
                } ?: run {
                    onLocationResult(null)
                }
            }
            .addOnFailureListener {
                onLocationResult(null)
            }
    } catch (e: SecurityException) {
        onLocationResult(null)
    }
}