package com.austral.gamerxandria.apiManager

import android.content.Context
import android.widget.Toast
import com.austral.gamerxandria.R
import com.austral.gamerxandria.model.VideoGame
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

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
            onSuccess = onSuccess,
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
        val requestExtraBody = "search \"%$name%\";"
        getGames(
            requestExtraBody = requestExtraBody,
            context = context,
            onSuccess = onSuccess,
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
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}
