package com.example.soccerverse.data.remote.responses.standingresponses

data class All(
    val draw: Int,
    val goals: Goals,
    val lose: Int,
    val played: Int,
    val win: Int
)