package com.example.soccerverse.navigation

import com.example.soccerverse.R


sealed class BottomNavItem(var title:String, var icon:Int){

    object Table : BottomNavItem("Table", R.drawable.ic_table)
    object Teams: BottomNavItem("Teams",R.drawable.ic_team)
//    object Matches: BottomNavItem("Matches",R.drawable.ic_match)

}