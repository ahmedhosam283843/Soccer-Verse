package com.example.soccerverse.data.models

data class LeagueStandingEntry(
    val teamPosition: Int,
    val teamName: String,
    val teamPoints: Int,
    val teamGoalDifference: Int,
    val teamPlayedGames: Int,
    val teamWon: Int,
    val teamDraw: Int,
    val teamLost: Int,
)
