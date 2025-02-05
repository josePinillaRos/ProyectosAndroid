package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Externals
 * se encarga de almacenar la información de los links externos
 *
 * @author Jose Pinilla
 */
data class Externals(
    @SerializedName("imdb")
    val imdb: String?,
    @SerializedName("thetvdb")
    val thetvdb: Int?,
    @SerializedName("tvrage")
    val tvrage: Int?
)