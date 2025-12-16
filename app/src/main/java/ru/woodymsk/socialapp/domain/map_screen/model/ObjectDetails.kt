package ru.woodymsk.socialapp.domain.map_screen.model

data class ObjectDetails(
    val title: String,
    val description: String,
    val coordinates: String,
    val uri: String?,
    val typeSpecificDetails: TypeSpecificDetails
)

// extended model
sealed class TypeSpecificDetails {
    data class Toponym(
        val address: String,
        val components: String? = null
    ) : TypeSpecificDetails()

    data class Business(
        val name: String,
        val workingHours: String?,
        val categories: String?,
        val phones: String?,
        val link: String?,
        val address: String?
    ) : TypeSpecificDetails()

    object Undefined : TypeSpecificDetails()
}