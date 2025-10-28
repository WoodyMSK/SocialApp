package ru.woodymsk.socialapp.presentation.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.woodymsk.socialapp.R

@Composable
fun YandexMap(
    initialCameraPosition: CameraPosition,
    points: List<Point> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                // установка начальной позиции камеры
                mapWindow.map.move(initialCameraPosition)

                // добавление метки для каждой точки
                points.forEach { point ->
                    val imageProvider = ImageProvider.fromResource(
                        context,
                        R.drawable.map_pin_red_149x149
                    )

                    mapWindow.map.mapObjects.addPlacemark().apply {
                        geometry = point
                        setIcon(imageProvider)
                    }
                }
            }
        },
        update = { mapView ->
            // обновление метки при изменении списка точек
            mapView.mapWindow.map.mapObjects.clear()
            points.forEach { point ->
                val imageProvider = ImageProvider.fromResource(
                    context,
                    R.drawable.map_pin_red_149x149
                )

                mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                    geometry = point
                    setIcon(imageProvider)
                }
            }
        }
    )
}