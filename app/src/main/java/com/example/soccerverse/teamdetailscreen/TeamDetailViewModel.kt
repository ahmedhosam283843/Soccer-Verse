package com.example.soccerverse.teamdetailscreen

import androidx.lifecycle.ViewModel
import com.example.soccerverse.data.models.TeamListEntry
import com.example.soccerverse.data.remote.responses.leagueresponses.LeagueList
import com.example.soccerverse.data.remote.responses.leagueteamsresponse.LeagueTeam
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val repository: SoccerVerseRepository
) : ViewModel() {

    suspend fun getTeamById(teamId: Int, leagueInt: Int): Resource<LeagueTeam> {
        return repository.getTeamById(2021, leagueInt, teamId)
    }

}