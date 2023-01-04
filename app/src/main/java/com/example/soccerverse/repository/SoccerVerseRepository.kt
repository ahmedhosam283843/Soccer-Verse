package com.example.soccerverse.repository

import com.example.soccerverse.data.remote.SoccerverseApi
import com.example.soccerverse.data.remote.TypeEnum
import com.example.soccerverse.data.remote.responses.leagueresponses.LeagueList
import com.example.soccerverse.data.remote.responses.leagueteamsresponse.LeagueTeam
import com.example.soccerverse.data.remote.responses.standingresponses.LeagueStanding
import com.example.soccerverse.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class SoccerVerseRepository @Inject constructor(
    private val api: SoccerverseApi
) {

    suspend fun getLeagueList(season: Int, type: TypeEnum) : Resource<LeagueList>{
        val response = try {
            api.getLeaguesLists(season, type)
        }catch (e: Exception){
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }

    suspend fun getLeagueById(season: Int, Id: Int) : Resource<LeagueList>{
        val response = try {
            api.getLeaguesById(season, Id)
        }catch (e: Exception){
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }

    suspend fun getStandings(season: Int, league: Int) : Resource<LeagueStanding>{
        val response = try {
            api.getStandings(season, league)
        }catch (e: Exception){
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }

    suspend fun getTeams(season: Int, league: Int) : Resource<LeagueTeam>{
        val response = try {
            api.getTeams(season, league)
        }catch (e: Exception){
            return Resource.Error(e.message)
        }
        return Resource.Success(response)
    }
}