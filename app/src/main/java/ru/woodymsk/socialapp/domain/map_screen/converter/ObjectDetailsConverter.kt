package ru.woodymsk.socialapp.domain.map_screen.converter

import com.yandex.mapkit.GeoObject
import ru.woodymsk.socialapp.domain.map_screen.model.ObjectDetails

interface ObjectDetailsConverter {
    fun convertGeoObjectToObjectDetails(geoObject: GeoObject?): ObjectDetails
}