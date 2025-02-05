package edu.josepinilla.demo05.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StateShows(
    @PrimaryKey
    val idShow: Int = 0,
    val stateFavorite: Boolean = false,
    val stateWatched: Boolean = false
)
