package com.example.soccerverse.data.remote.responses

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)