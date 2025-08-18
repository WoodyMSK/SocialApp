package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.woodymsk.socialapp.data.event.model.EventDTO

interface EventService {

    @POST("api/events")
    suspend fun createEvent(
        @Body eventCreate: EventDTO
    ): Response<EventDTO>

    @DELETE("api/events/{event_id}")
    suspend fun removeEventById(
        @Path("event_id") id: String,
    ): Response<Unit>

    @GET("api/events/{event_id}/before")
    suspend fun getBeforeEvent(
        @Path("event_id") id: String,
        @Query("count") count: Int,
    ): Response<List<EventDTO>>

    @GET("api/events/latest")
    suspend fun getLatest(
        @Query("count") count: Int)
    : Response<List<EventDTO>>
}