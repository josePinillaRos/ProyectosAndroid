package edu.josepinilla.filmlist.model

import edu.josepinilla.filmlist.utils.Utils

data class Film(
    val id: Int,
    val title: String,
    val year: Int,
    val duration: Int,
    val genre: String,
    val director: String,
    val cover: String
) {
    companion object {
        var films: MutableList<Film> = mutableListOf()
    }
}
