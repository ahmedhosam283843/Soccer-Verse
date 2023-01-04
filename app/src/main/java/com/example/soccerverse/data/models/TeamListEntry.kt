package com.example.soccerverse.data.models

data class TeamListEntry(
    val teamName: String,
    val imageUrl: String?,
    val number: Int,
    val country: String?,
    val founded: Int?,
    val venueName: String?,
    val venueCapacity: Int?,
    val venueImageUrl: String?,
)