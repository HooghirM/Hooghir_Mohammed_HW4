package com.example.hooghirmohammedhw4

import android.view.KeyEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TicketMasterApi {
    @GET("events")
    fun searchEvents (
        @Query("apikey") apiKey: String,
        @Query("keyword") searchTerm: String,
        @Query("city") city: String
    ): Call<EventResponse>
}