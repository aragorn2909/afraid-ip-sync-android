package com.example.afraidipsync

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/status")
    suspend fun getStatus(): Response<StatusResponse>

    @POST("sync_all")
    suspend fun syncAll(): Response<Unit>
}
