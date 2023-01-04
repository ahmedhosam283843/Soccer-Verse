package com.example.soccerverse.data.remote.responses.standingresponses

data class Home(
    val draw: Int,
    val goals: Goals,
    val lose: Int,
    val played: Int,
    val win: Int
)