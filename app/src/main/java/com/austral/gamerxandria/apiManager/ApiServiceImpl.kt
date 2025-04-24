package com.austral.gamerxandria.apiManager

import android.content.Context
import android.widget.Toast
import com.austral.gamerxandria.R
import com.austral.gamerxandria.model.VideoGame
import com.austral.gamerxandria.model.VideoGameName
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject
import kotlin.collections.map

class ApiServiceImpl @Inject constructor() {
    fun getVideoGamesByIds(
        listOfIds: List<Int>,
        context: Context,
        onSuccess: (List<VideoGame>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val requestExtraBody = "where id = (${listOfIds.joinToString(",")}); limit ${listOfIds.size};"
        getGames(
            requestExtraBody = requestExtraBody,
            context = context,
            onSuccess = {
                onSuccess(it.map { it.cover?.url = it.cover.url.replace("t_thumb", "t_720p"); it})
                        },
            onFail = onFail,
            loadingFinished = loadingFinished
        )
    }

    fun getVideoGameById(
        id: Int,
        context: Context,
        onSuccess: (VideoGame) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val requestExtraBody = "where id = $id; limit 1;"
        getGames(
            requestExtraBody = requestExtraBody,
            context = context,
            onSuccess = { games ->
                if (games.isNotEmpty()) {
                    if (games[0].cover != null) {
                        games[0].cover?.url = games[0].cover?.url?.replace("t_thumb", "t_720p").toString()
                    }
                    onSuccess(games[0])
                } else {
                    onFail()
                }
            },
            onFail = onFail,
            loadingFinished = loadingFinished
        )
    }

    fun searchVideoGamesByName(
        name: String,
        context: Context,
        onSuccess: (List<VideoGame>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val requestExtraBody = if (name.isBlank()) { ""} else { "search \"%$name%\";" }
        getGames(
            requestExtraBody = requestExtraBody,
            context = context,
            onSuccess = {
                onSuccess(it.map { if (it.cover != null) { it.cover.url = it.cover.url.replace("t_thumb", "t_720p") }; it})
            },
            onFail = onFail,
            loadingFinished = loadingFinished
        )
    }

    fun searchVideoGamesNames(
        guessedName: String,
        context: Context,
        onSuccess: (List<VideoGameName>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val requestExtraBody = "search \"$guessedName\"; limit 10;"
        getGames(
            requestExtraBody = requestExtraBody,
            context = context,
            onSuccess = {games ->
                val videoGameNames = games.map { VideoGameName(it.id, it.name) }
                onSuccess(videoGameNames)
            },
            onFail = onFail,
            loadingFinished = loadingFinished
        )
    }

    private fun getGames(
        requestExtraBody: String,
        context: Context,
        onSuccess: (List<VideoGame>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                context.getString(R.string.games_url)
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<List<VideoGame>> = service.getGames(
            clientId = context.getString(R.string.client_id),
            authorization = context.getString(R.string.access_token),
            requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields name, cover.url, platforms.name, genres.name, involved_companies.company.name, summary, first_release_date;$requestExtraBody")
        )

        call.enqueue(object : Callback<List<VideoGame>> {
            override fun onResponse(response: Response<List<VideoGame>>?, retrofit: Retrofit?) {
                loadingFinished()
                if(response?.isSuccess == true) {
                    val games: List<VideoGame> = response.body()
                    onSuccess(games)
                } else {
                    onFailure(Exception(response?.message()))
                }
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, "Couldn't retrieve game", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}
