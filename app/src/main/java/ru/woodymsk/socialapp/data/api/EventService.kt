package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.woodymsk.socialapp.data.event.model.EventDTO

interface EventService {

    @GET("api/events")
    suspend fun getAllEventList(): Response<List<EventDTO>>

    @POST("api/events")
    suspend fun createEvent(
        @Body eventCreate: EventDTO
    ): Response<EventDTO>

    @DELETE("api/events/{event_id}")
    suspend fun removeEventById(
        @Path("event_id") id: String,
    ): Response<Unit>
}