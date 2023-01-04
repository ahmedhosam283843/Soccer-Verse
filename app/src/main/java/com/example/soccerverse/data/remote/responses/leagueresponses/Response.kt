package com.example.soccerverse.data.remote.responses.leagueresponses

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)