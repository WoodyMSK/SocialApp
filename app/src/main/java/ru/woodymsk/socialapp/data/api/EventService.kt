package ru.woodymsk.socialapp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.woodymsk.socialapp.data.event.model.EventDTO

interface EventService {

    @GET("api/events")
    suspend fun getAllEventList(): Response<List<EventDTO>>

    @POST("api/events")
    suspend fun createEvent(
        @Body eventCreate: EventDTO
    ): Response<EventDTO>

}