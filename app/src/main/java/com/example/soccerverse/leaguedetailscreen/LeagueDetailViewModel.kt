package com.example.soccerverse.leaguedetailscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccerverse.data.models.LeagueListEntry
import com.example.soccerverse.data.models.LeagueStandingEntry
import com.example.soccerverse.data.models.TeamListEntry
import com.example.soccerverse.data.remote.TypeEnum
import com.example.soccerverse.data.remote.responses.leagueresponses.LeagueList
import com.example.soccerverse.data.remote.responses.standingresponses.LeagueStanding
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueDetailViewModel @Inject constructor(
    private val repository: SoccerVerseRepository
) : ViewModel() {
    var leagueStanding = mutableStateOf<List<LeagueStandingEntry>>(listOf())
    var teamList = mutableStateOf<List<TeamListEntry>>(listOf())




    suspend fun getLeagueById(leagueId: Int): Resource<LeagueList> {
        return repository.getLeagueById(2022, leagueId)
    }


    fun getLeagueTeams(leagueId: Int) {
        viewModelScope.launch {
            val result = repository.getTeams(2021, leagueId)
            when (result) {
                is Resource.Success -> {
                    val teamEntries: List<TeamListEntry> =
                        result.data!!.response.mapIndexed { index, entry ->
                            TeamListEntry(
                                entry.team.name,
                                entry.team.logo,
                                entry.team.id,
                                entry.team.country,
                                entry.team.founded,
                                entry.venue.name,
                                entry.venue.capacity,
                                entry.venue.image,
                            )
                        }
                    teamList.value = teamEntries
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }
    }

    fun loadLeagueStanding(leagueId: Int) {
        viewModelScope.launch {
            val result = repository.getStandings(2022, leagueId)
            when (result) {
                is Resource.Success -> {
                    result.data!!.response[0].league.standings[0].forEach {
                        leagueStanding.value += LeagueStandingEntry(
                            it.rank,
                            it.team.name,
                            it.points,
                            it.goalsDiff,
                            it.all.played,
                            it.all.win,
                            it.all.draw,
                            it.all.lose,
                        )
                    }
                }
                is Resource.Error -> {

                }
                else -> {}
            }

        }
    }
}