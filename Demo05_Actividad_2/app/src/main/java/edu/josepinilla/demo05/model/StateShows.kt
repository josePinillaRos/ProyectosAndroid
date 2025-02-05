package edu.josepinilla.demo05.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * data class StateShows
 * se encarga de almacenar la información del estado de los shows
 *
 * @param idShow id del show
 * @param stateFavorite estado de favorito
 * @param stateWatched estado de visto
 *
 * @author Jose Pinilla
 */
@Entity
data class StateShows(
    @PrimaryKey
    val idShow: Int = 0,
    val stateFavorite: Boolean = false,
    val stateWatched: Boolean = false
)
