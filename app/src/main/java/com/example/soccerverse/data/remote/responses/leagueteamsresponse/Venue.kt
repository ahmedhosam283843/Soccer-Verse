package com.example.soccerverse.data.remote.responses.leagueteamsresponse

data class Venue(
    val address: String,
    val capacity: Int,
    val city: String,
    val id: Int,
    val image: String,
    val name: String,
    val surface: String
)