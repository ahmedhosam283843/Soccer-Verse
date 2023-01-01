package com.example.soccerverse.data.remote

import com.example.soccerverse.data.remote.responses.LeagueList
import retrofit2.http.GET
import retrofit2.http.Query


interface SoccerverseApi {
    @GET("leagues")
    suspend fun getLeaguesLists(
        @Query("season") season: Int?,
        @Query("type") type: TypeEnum?
    ): LeagueList

}