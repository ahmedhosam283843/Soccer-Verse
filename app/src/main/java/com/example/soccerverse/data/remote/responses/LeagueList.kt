package com.example.soccerverse.data.remote.responses

data class LeagueList(
    val errors: List<Any>,
    val get: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)