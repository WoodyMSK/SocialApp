package ru.woodymsk.socialapp.presentation.map_screen.converter

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.mapkit.uri.UriObjectMetadata
import ru.woodymsk.socialapp.R
import ru.woodymsk.socialapp.domain.map_screen.converter.ObjectDetailsConverter
import ru.woodymsk.socialapp.presentation.common.ResourcesService
import ru.woodymsk.socialapp.domain.map_screen.model.ObjectDetails
import ru.woodymsk.socialapp.domain.map_screen.model.TypeSpecificDetails
import javax.inject.Inject


class ObjectDetailsConverterImpl @Inject constructor(
    private val resourcesService: ResourcesService,
) : ObjectDetailsConverter {
    override fun convertGeoObjectToObjectDetails(geoObject: GeoObject?): ObjectDetails {
        if (geoObject == null) {
            return ObjectDetails(
                title = resourcesService.getString(R.string.name_is_missing),
                description = resourcesService.getString(R.string.description_is_missing),
                coordinates = resourcesService.getString(R.string.coordinates_not_specified),
                uri = null,
                typeSpecificDetails = TypeSpecificDetails.Undefined,
            )
        }

        return with(geoObject) {
            val uri = metadataContainer
                .getItem(UriObjectMetadata::class.java)
                ?.uris
                ?.firstOrNull()
                ?.value

            val typeSpecificDetails = metadataContainer.getItem(ToponymObjectMetadata::class.java)
                ?.let { toponym -> createToponymDetails(toponym) }
                ?: metadataContainer.getItem(BusinessObjectMetadata::class.java)
                    ?.let { business -> createBusinessDetails(business) }
                ?: TypeSpecificDetails.Undefined

            val coordinates = this.geometry
                .firstOrNull()
                ?.point
                ?.let { point -> "${point.latitude}, ${point.longitude}" }
                ?: resourcesService.getString(R.string.coordinates_not_specified)

            ObjectDetails(
                title = name ?: resourcesService.getString(R.string.name_is_missing),
                description = descriptionText
                    ?: resourcesService.getString(R.string.description_is_missing),
                coordinates = coordinates,
                uri = uri,
                typeSpecificDetails = typeSpecificDetails,
            )
        }
    }

    private fun createToponymDetails(toponym: ToponymObjectMetadata): TypeSpecificDetails.Toponym {
        return TypeSpecificDetails.Toponym(
            address = toponym.address.formattedAddress,
            components = toponym.address.components
                .takeIf { it.isNotEmpty() }
                ?.joinToString { it.name }
                .orEmpty()
        )
    }

    private fun createBusinessDetails(business: BusinessObjectMetadata): TypeSpecificDetails.Business {
        return TypeSpecificDetails.Business(
            name = business.name,
            workingHours = business.workingHours?.text,
            categories = business.categories
                .map { it.name }
                .takeIf { it.isNotEmpty() }
                ?.toSet()
                ?.joinToString()
                .orEmpty(),
            phones = business.phones
                .map { it.formattedNumber }
                .takeIf { it.isNotEmpty() }
                ?.joinToString()
                .orEmpty(),
            link = business.links.firstOrNull()?.link?.href,
            address = business.address.formattedAddress
        )
    }
}