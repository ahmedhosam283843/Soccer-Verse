package com.example.soccerverse.leaguedetailscreen

import androidx.lifecycle.ViewModel
import com.example.soccerverse.data.remote.responses.LeagueList
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeagueDetailViewModel @Inject constructor(
    private val repository: SoccerVerseRepository
) : ViewModel() {
    suspend fun getLeagueById(leagueId: Int): Resource<LeagueList> {
        return repository.getLeagueById(2022, leagueId)
    }
}