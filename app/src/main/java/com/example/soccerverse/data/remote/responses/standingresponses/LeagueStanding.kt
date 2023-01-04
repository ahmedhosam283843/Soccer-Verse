package com.example.soccerverse.data.remote.responses.standingresponses

data class LeagueStanding(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)