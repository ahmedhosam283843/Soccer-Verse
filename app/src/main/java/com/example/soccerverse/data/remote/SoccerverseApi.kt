package com.example.soccerverse.data.remote

import com.example.soccerverse.data.remote.responses.leagueresponses.LeagueList
import com.example.soccerverse.data.remote.responses.leagueteamsresponse.LeagueTeam
import com.example.soccerverse.data.remote.responses.standingresponses.LeagueStanding
import retrofit2.http.GET
import retrofit2.http.Query


interface SoccerverseApi {
    @GET("leagues")
    suspend fun getLeaguesLists(
        @Query("season") season: Int?,
        @Query("type") type: TypeEnum?
    ): LeagueList

    @GET("leagues")
    suspend fun getLeaguesById(
        @Query("season") season: Int?,
        @Query("id") id: Int?
    ): LeagueList

    @GET("standings")
    suspend fun getStandings(
        @Query("season") season: Int?,
        @Query("league") league: Int?
    ): LeagueStanding

    @GET("teams")
    suspend fun getTeams(
        @Query("season") season: Int?,
        @Query("league") league: Int?
    ): LeagueTeam

    @GET("teams")
    suspend fun getTeamById(
        @Query("id") id: Int?,
        @Query("league") league: Int?,
        @Query("season") season: Int?,
    ): LeagueTeam



}