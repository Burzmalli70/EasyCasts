package dev.joewilliams.easycasts.network

import dev.joewilliams.easycasts.model.ItunesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?media=podcast&limit=25")
    suspend fun searchPodcasts(
        @Query("term") terms: String
    ): Response<ItunesResponse>
}