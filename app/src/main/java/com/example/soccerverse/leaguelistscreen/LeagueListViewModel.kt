package com.example.soccerverse.leaguelistscreen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.soccerverse.data.models.LeagueListEntry
import com.example.soccerverse.data.remote.TypeEnum
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueListViewModel @Inject constructor(
    private val repository: SoccerVerseRepository
) : ViewModel() {
    var leagueList = mutableStateOf<List<LeagueListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedLeagueList = listOf<LeagueListEntry>()
    private var isSearchStarting = true
    val isSearching = mutableStateOf(false)


    init {
        loadLeagueList()
    }

     fun loadLeagueList(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getLeagueList(2022, TypeEnum.league)
            when(result){
                is Resource.Success->{
                    val leagueEntries: List<LeagueListEntry> = result.data!!.response.mapIndexed { index, entry ->
                        val imageUrl = entry.league.logo
                        LeagueListEntry(entry.league.name, imageUrl, entry.league.id)
                    }

                    loadError.value = ""
                    leagueList.value = leagueEntries
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }

        }
    }

    fun searchLeagueList(query: String) {
        val listToSearch = if (isSearchStarting) {
            leagueList.value
        } else {
            cachedLeagueList
        }
        viewModelScope.launch (Dispatchers.Default){
            if (query.isEmpty()){
                leagueList.value = cachedLeagueList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.leagueName.contains(query.trim(), ignoreCase = true)
                        || it.number.toString() == query.trim()
            }
            if (isSearchStarting){
                cachedLeagueList = leagueList.value
                isSearchStarting = false
            }
            leagueList.value = results
            isSearching.value = true
        }

    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }


}