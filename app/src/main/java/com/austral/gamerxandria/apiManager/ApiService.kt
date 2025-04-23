package com.austral.gamerxandria.apiManager

import com.austral.gamerxandria.model.VideoGame
import com.squareup.okhttp.RequestBody
import retrofit.Call
import retrofit.http.Body
import retrofit.http.POST
import retrofit.http.Header

interface ApiService {
    @POST("games")
    fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ): Call<List<VideoGame>>
}
