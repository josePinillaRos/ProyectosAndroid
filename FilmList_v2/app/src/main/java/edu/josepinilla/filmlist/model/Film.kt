package edu.josepinilla.filmlist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class Film parcelada
 */
@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val year: Int,
    val duration: Int,
    val genre: String,
    val director: String,
    val cover: String
) :Parcelable{
    companion object {
        var films: MutableList<Film> = mutableListOf()
    }
}
